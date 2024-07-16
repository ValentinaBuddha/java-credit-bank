package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Create loan offers.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LoanOfferService {

    private final AnnuityCalculatorService annuityCalculatorService;

    private final RateCalculatorService rateCalculatorService;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Calculating loan offers for {}", loanStatement);

        return Stream.of(
                        calculateLoanOffer(false, false, loanStatement),
                        calculateLoanOffer(true, false, loanStatement),
                        calculateLoanOffer(false, true, loanStatement),
                        calculateLoanOffer(true, true, loanStatement))
                .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
                .toList();
    }

    private LoanOfferDto calculateLoanOffer(Boolean isInsuranceEnabled,
                                           Boolean isSalaryClient,
                                           LoanStatementRequestDto loanStatement) {
        log.info("Calculating offer: isInsuranceEnabled = {}, isSalaryClient = {}", isInsuranceEnabled, isSalaryClient);

        var totalAmount = annuityCalculatorService.calculateTotalAmount(
                loanStatement.getAmount(), isInsuranceEnabled);

        var rate = rateCalculatorService.calculateRate(
                isInsuranceEnabled, isSalaryClient);

        var monthlyPayment = annuityCalculatorService.calculateMonthlyPayment(
                totalAmount, loanStatement.getTerm(), rate);

        return LoanOfferDto.builder()
                .requestedAmount(loanStatement.getAmount())
                .totalAmount(totalAmount)
                .term(loanStatement.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
}