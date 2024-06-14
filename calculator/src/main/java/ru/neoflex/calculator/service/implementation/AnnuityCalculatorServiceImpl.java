package ru.neoflex.calculator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.RateConfiguration;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.service.AnnuityCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.neoflex.calculator.util.BigDecimalConstant.HUNDRED;
import static ru.neoflex.calculator.util.BigDecimalConstant.MONTHS_IN_YEAR;
import static ru.neoflex.calculator.util.BigDecimalConstant.ONE_HUNDREDTH;

/**
 * Calculate parameters of credit with annuity payments.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AnnuityCalculatorServiceImpl implements AnnuityCalculatorService {

    private final RateConfiguration rateConfiguration;

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        log.info("Calculating total amount: amount = {}, isInsuranceEnabled = {}", amount, isInsuranceEnabled);

        if (Boolean.TRUE.equals(isInsuranceEnabled)) {
            return amount
                    .add(amount
                            .multiply(BigDecimal.valueOf(rateConfiguration.getInsuranceRate()))
                            .multiply(ONE_HUNDREDTH))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) {
        log.info("Calculating monthly payment: totalAmount = {}, term = {}, rate = {}",
                totalAmount, term, rate);

        var monthlyRate = rate.divide(MONTHS_IN_YEAR, 5, RoundingMode.HALF_UP).multiply(ONE_HUNDREDTH);

        var x = monthlyRate.add(BigDecimal.ONE).pow(term);

        var y = monthlyRate.multiply(x);

        var z = x.subtract(BigDecimal.ONE);

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
        var dateOfIssue = LocalDate.now();
        var remainingDebt = amount;
        var totalPayment = monthlyPayment;
        var monthlyRate = rate
                .multiply(ONE_HUNDREDTH)
                .divide(MONTHS_IN_YEAR, 10, RoundingMode.HALF_UP);

        var loanIssue = PaymentScheduleElementDto.builder()
                .number(0)
                .date(dateOfIssue)
                .totalPayment(BigDecimal.ZERO)
                .interestPayment(BigDecimal.ZERO)
                .debtPayment(BigDecimal.ZERO)
                .remainingDebt(remainingDebt)
                .build();

        paymentSchedule.add(loanIssue);

        for (int i = 1; i < term + 1; i++) {

            var currentDate = dateOfIssue.plusMonths(i - 1L);
            var interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            var debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            if (i == term && remainingDebt.compareTo(BigDecimal.ZERO) != 0) {
                totalPayment = totalPayment.add(remainingDebt);
                debtPayment = totalPayment.subtract(interestPayment);
                remainingDebt = BigDecimal.ZERO;
            }

            var paymentScheduleElement = PaymentScheduleElementDto.builder()
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
    public BigDecimal calculatePsk(List<PaymentScheduleElementDto> paymentSchedule, BigDecimal amount, Integer term) {
        log.info("Calculating psk: amount = {}, term = {}", amount, term);

        var totalAmount = paymentSchedule.stream()
                .map(PaymentScheduleElementDto::getTotalPayment)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var termInYears = new BigDecimal(term).divide(MONTHS_IN_YEAR, 5, RoundingMode.HALF_UP);

        return ((totalAmount
                .divide(amount, 7, RoundingMode.HALF_UP))
                .subtract(BigDecimal.ONE))
                .divide(termInYears, 7, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .setScale(3, RoundingMode.HALF_UP);
    }
}