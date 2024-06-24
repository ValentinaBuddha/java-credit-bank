package ru.neoflex.calculator.service;

import ru.neoflex.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Describes the functionality of the service for credit parameters calculation with annuity payments.
 *
 * @author Valentina Vakhlamova
 */
public interface AnnuityCalculatorService {

    BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled);

    BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate);

    List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount,
                                                             Integer term,
                                                             BigDecimal rate,
                                                             BigDecimal monthlyPayment);

    BigDecimal calculatePsk(List<PaymentScheduleElementDto> paymentSchedule, BigDecimal amount, Integer term);
}