package ru.neoflex.calculator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.AnnuityCalculatorService;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.RateCalculatorService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Calculate final credit parameters.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {

    private final AnnuityCalculatorService annuityCalculatorService;

    private final RateCalculatorService rateCalculatorService;

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.info("Calculating credit {}", scoringData);

        BigDecimal rate = rateCalculatorService.calculateRate(
                scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        rate = rateCalculatorService.calculateFinalRate(
                scoringData, rate);

        BigDecimal amount = annuityCalculatorService.calculateTotalAmount(
                scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

        BigDecimal monthlyPayment = annuityCalculatorService.calculateMonthlyPayment(
                amount, scoringData.getTerm(), rate);

        List<PaymentScheduleElementDto> paymentSchedule = annuityCalculatorService.calculatePaymentSchedule(
                amount, scoringData.getTerm(), rate, monthlyPayment);

        BigDecimal psk = annuityCalculatorService.calculatePSK(
                paymentSchedule, scoringData.getAmount(), scoringData.getTerm());

        return CreditDto.builder()
                .amount(amount)
                .term(scoringData.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
                .isSalaryClient(scoringData.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
}
