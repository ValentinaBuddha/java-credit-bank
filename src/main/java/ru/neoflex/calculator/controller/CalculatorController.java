package ru.neoflex.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOfferService;

import javax.validation.Valid;
import java.util.List;

/**
 * API for validation and credit parameters calculation.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Кредитный калькулятор", description = "API по расчету параметров кредита и скорингу данных.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final LoanOfferService loanOfferService;

    private final CreditService creditService;

    /**
     * Validate data and calculate 4 loan offers.
     */
    @Operation(summary = "Расчёт возможных условий кредита и прескоринг данных.")
    @PostMapping("/offers")
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                                  LoanStatementRequestDto loanStatement) {
        return loanOfferService.calculateLoanOffers(loanStatement);
    }

    /**
     * Validate full data and calculate final credit parameters.
     */
    @Operation(summary = "Полный расчёт параметров кредита, скоринг и валидация данных.")
    @PostMapping("/calc")
    public CreditDto calculateCredit(@Parameter(required = true) @Valid @RequestBody ScoringDataDto scoringData) {

        return creditService.calculateCredit(scoringData);
    }
}