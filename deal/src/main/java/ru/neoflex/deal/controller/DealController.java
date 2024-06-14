package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.service.DealService;

import javax.validation.Valid;
import java.util.List;

/**
 * API.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Сделка", description = "API.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;

    @Operation(summary = "Расчёт возможных условий кредита.")
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                                  LoanStatementRequestDto loanStatement) {
        return dealService.calculateLoanOffers(loanStatement);
    }

    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/offer/select")
    public void selectLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                 LoanOfferDto loanOffer) {
        dealService.selectLoanOffers(loanOffer);
    }

    @Operation(summary = "Завершение регистрации и полный подсчёт кредита.")
    @PostMapping("/calculate/{statementId}")
    public void calculateCredit(@PathVariable @Parameter(required = true) String statementId,
                                @Parameter(required = true) @Valid @RequestBody
                                FinishRegistrationRequestDto finishRegistration) {
        dealService.finishRegistration(statementId, finishRegistration);
    }
}