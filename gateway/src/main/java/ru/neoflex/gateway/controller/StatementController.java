package ru.neoflex.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;
import ru.neoflex.gateway.exception.PrescoringException;
import ru.neoflex.gateway.exception.StatementStatusException;
import ru.neoflex.gateway.feign.DealFeignClient;
import ru.neoflex.gateway.feign.StatementFeignClient;

import javax.validation.Valid;
import java.util.List;

import static ru.neoflex.gateway.enums.Status.APPROVED;
import static ru.neoflex.gateway.enums.Status.CC_DENIED;
import static ru.neoflex.gateway.enums.Status.PREAPPROVAL;

/**
 * API for sending requests to StatementMS and DealMS.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Gateway: заявка", description = "Отправляет запросы в StatementMS и DealMS.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/statement")
public class StatementController {

    private final StatementFeignClient statementFeignClient;
    private final DealFeignClient dealFeignClient;

    /**
     * Validate data and calculate 4 loan offers.
     */
    @Operation(summary = "Прескоринг данных и расчёт возможных условий кредита.")
    @PostMapping
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                                  LoanStatementRequestDto loanStatement) {
        List<LoanOfferDto> offers = statementFeignClient.calculateLoanOffers(loanStatement);
        if (offers.isEmpty()) {
            throw new PrescoringException("Prescoring failed.");
        } else {
            return offers;
        }
    }

    /**
     * Select one loan offer.
     */
    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/select")
    public void selectLoanOffers(@Parameter(required = true) @RequestBody @Valid LoanOfferDto loanOffer) {
        var statement = dealFeignClient.findStatementById(loanOffer.getStatementId().toString());
        if (statement.getStatus() == PREAPPROVAL) {
            statementFeignClient.selectLoanOffers(loanOffer);
        } else if (statement.getStatus() == CC_DENIED) {
            throw new StatementStatusException("Statement was denied.");
        } else {
            throw new StatementStatusException("For choosing loan offer statement status should be PREAPPROVAL.");
        }
    }

    /**
     * Final registration and full calculation of credit parameters.
     */
    @Operation(summary = "Завершение регистрации и полный расчёт всех параметров по кредиту.")
    @PostMapping("/registration/{statementId}")
    public void finishRegistration(@PathVariable @Parameter(required = true) String statementId,
                                   @Parameter(required = true) @Valid @RequestBody
                                   FinishRegistrationRequestDto finishRegistration) {
        var statement = dealFeignClient.findStatementById(statementId);
        if (statement.getStatus() == APPROVED) {
            dealFeignClient.finishRegistration(statementId, finishRegistration);
        } else if (statement.getStatus() == CC_DENIED) {
            throw new StatementStatusException("Statement was denied.");
        } else {
            throw new StatementStatusException("For choosing loan offer statement status should be APPROVED.");
        }
    }
}