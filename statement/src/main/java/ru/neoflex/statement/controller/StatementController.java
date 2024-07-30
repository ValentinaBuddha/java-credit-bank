package ru.neoflex.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.dto.LoanStatementRequestWrapper;
import ru.neoflex.statement.exception.PrescoringException;
import ru.neoflex.statement.feign.DealFeignClient;
import ru.neoflex.statement.service.PreskoringService;

import javax.validation.Valid;
import java.util.List;

import static ru.neoflex.statement.enums.Status.CLIENT_DENIED;
import static ru.neoflex.statement.enums.Status.PREAPPROVAL;

/**
 * API for prescoring, calculation of loan offers and choosing one of them.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Заявка", description = "API по прескорингу данных, расчёту возможных условий кредита и выбору одного из них.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/statement")
public class StatementController {

    private final DealFeignClient dealFeignClient;
    private final PreskoringService preskoringService;

    /**
     * Validate data and calculate 4 loan offers.
     */
    @Operation(summary = "Прескоринг данных и расчёт возможных условий кредита.")
    @PostMapping
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @RequestBody @Valid
                                                      LoanStatementRequestDto loanStatement) {
        try {
            preskoringService.prescoring(loanStatement);
            return dealFeignClient.calculateLoanOffers(new LoanStatementRequestWrapper(loanStatement, PREAPPROVAL));
        } catch (PrescoringException exception) {
            return dealFeignClient.calculateLoanOffers(new LoanStatementRequestWrapper(loanStatement, CLIENT_DENIED));
        }
    }

    /**
     * Select one loan offer.
     */
    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/offer")
    public void selectLoanOffers(@Parameter(required = true) @RequestBody @Valid LoanOfferDto loanOffer) {
        dealFeignClient.selectLoanOffers(loanOffer);
    }
}