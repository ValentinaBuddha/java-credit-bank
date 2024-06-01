package ru.neoflex.calculator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.ApplicationConfig;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.service.AnnuityCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.neoflex.calculator.util.BigDecimalConstant.*;

/**
 * Calculate parameters of credit with annuity payments.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AnnuityCalculatorServiceImpl  implements AnnuityCalculatorService {

    private final ApplicationConfig applicationConfig;

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        log.info("Calculating total amount: amount = {}, isInsuranceEnabled = {}", amount, isInsuranceEnabled);

        if (isInsuranceEnabled) {
            return amount
                    .add(amount
                            .multiply(applicationConfig.getInsuranceRate())
                            .multiply(ONE_HUNDREDTH));
        } else {
            return amount;
        }
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) {
        log.info("Calculating monthly payment: totalAmount = {}, term = {}, rate = {}",
                totalAmount, term, rate);

        BigDecimal monthlyRate = rate.divide(MONTHS_IN_YEAR, 5, RoundingMode.HALF_UP).multiply(ONE_HUNDREDTH);

        BigDecimal x = monthlyRate.add(BigDecimal.ONE).pow(term);

        BigDecimal y = monthlyRate.multiply(x);

        BigDecimal z = x.subtract(BigDecimal.ONE);

        return totalAmount.multiply(y).divide(z, 2, RoundingMode.HALF_UP);
    }

    @Override
    public List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount,
                                                                    Integer term,
                                                                    BigDecimal rate,
                                                                    BigDecimal monthlyPayment) {
        log.info("Calculating payment schedule: amount = {}, term = {}, rate = {}, monthlyPayment = {}",
                amount, term, rate, monthlyPayment);

        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        LocalDate dateOfIssue = LocalDate.now();
        BigDecimal remainingDebt = amount;
        BigDecimal totalPayment = monthlyPayment;
        BigDecimal monthlyRate = rate
                .multiply(ONE_HUNDREDTH)
                .divide(MONTHS_IN_YEAR, 10, RoundingMode.HALF_UP);

        PaymentScheduleElementDto loanIssue = PaymentScheduleElementDto.builder()
                .number(0)
                .date(dateOfIssue)
                .totalPayment(BigDecimal.ZERO)
                .interestPayment(BigDecimal.ZERO)
                .debtPayment(BigDecimal.ZERO)
                .remainingDebt(remainingDebt)
                .build();

        paymentSchedule.add(loanIssue);

        for (int i = 1; i < term + 1; i++) {

            LocalDate currentDate = dateOfIssue.plusMonths(i - 1);
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            if (i == term && remainingDebt.compareTo(BigDecimal.ZERO) != 0) {
                totalPayment = totalPayment.add(remainingDebt);
                debtPayment = totalPayment;
                remainingDebt = BigDecimal.ZERO;
            }

            PaymentScheduleElementDto paymentScheduleElement = PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(currentDate)
                    .totalPayment(totalPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build();

            paymentSchedule.add(paymentScheduleElement);
        }
        return paymentSchedule;
    }

    @Override
    public BigDecimal calculatePSK(List<PaymentScheduleElementDto> paymentSchedule, BigDecimal amount, Integer term) {
        log.info("Calculating psk: amount = {}, term = {}", amount, term);

        BigDecimal totalAmount = paymentSchedule.stream()
                .map(PaymentScheduleElementDto::getTotalPayment)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal termInYears = new BigDecimal(term).divide(MONTHS_IN_YEAR, 5, RoundingMode.HALF_UP);

        return (totalAmount
                .divide(amount, 5, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE))
                .divide(termInYears, 5, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .setScale(2, RoundingMode.HALF_UP);
    }
}