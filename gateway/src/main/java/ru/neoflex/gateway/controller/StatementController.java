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
import ru.neoflex.gateway.feign.DealFeignClient;
import ru.neoflex.gateway.feign.StatementFeignClient;

import javax.validation.Valid;
import java.util.List;

/**
 * API for sending requests for StatementMS and DealMS.
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
        return statementFeignClient.calculateLoanOffers(loanStatement);
    }

    /**
     * Select one loan offer.
     */
    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/select")
    public void selectLoanOffers(@Parameter(required = true) @RequestBody @Valid LoanOfferDto loanOffer) {
        statementFeignClient.selectLoanOffers(loanOffer);
    }

    /**
     * Final registration and full calculation of credit parameters.
     */
    @Operation(summary = "Завершение регистрации и полный расчёт всех параметров по кредиту.")
    @PostMapping("/registration/{statementId}")
    public void finishRegistration(@PathVariable @Parameter(required = true) String statementId,
                                   @Parameter(required = true) @Valid @RequestBody
                                   FinishRegistrationRequestDto finishRegistration) {
        dealFeignClient.finishRegistration(statementId, finishRegistration);
    }


}