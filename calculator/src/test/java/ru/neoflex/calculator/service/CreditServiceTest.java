package ru.neoflex.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.neoflex.calculator.enums.EmploymentStatus.EMPLOYED;
import static ru.neoflex.calculator.enums.Gender.FEMALE;
import static ru.neoflex.calculator.enums.MaritalStatus.WIDOW_WIDOWER;
import static ru.neoflex.calculator.enums.Position.WORKER;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private ScoringService scoringService;

    @Mock
    private AnnuityCalculatorService annuityCalculator;

    @Mock
    private RateCalculatorService rateCalculator;

    @InjectMocks
    private CreditService creditService;

    private BigDecimal rate = BigDecimal.valueOf(19);
    private BigDecimal amount = BigDecimal.valueOf(100000);
    private int term = 1;
    private BigDecimal monthlyPayment = BigDecimal.valueOf(101583.33);
    private BigDecimal psk = BigDecimal.valueOf(1.56);
    private PaymentScheduleElementDto paymentScheduleElementDto = PaymentScheduleElementDto.builder()
            .number(1)
            .date(LocalDate.now().plusMonths(1))
            .totalPayment(monthlyPayment)
            .interestPayment(BigDecimal.valueOf(1557.38))
            .debtPayment(amount)
            .remainingDebt(BigDecimal.ZERO)
            .build();
    private List<PaymentScheduleElementDto> paymentSchedule = List.of(paymentScheduleElementDto);
    private EmploymentDto employment = EmploymentDto.builder()
            .employmentStatus(EMPLOYED)
            .position(WORKER)
            .build();
    private ScoringDataDto scoringData = ScoringDataDto.builder()
            .amount(amount)
            .term(term)
            .gender(FEMALE)
            .birthdate(LocalDate.now().minusYears(30))
            .maritalStatus(WIDOW_WIDOWER)
            .employment(employment)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();
    private CreditDto credit = CreditDto.builder()
            .amount(amount)
            .term(term)
            .monthlyPayment(monthlyPayment)
            .rate(rate)
            .psk(psk)
            .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
            .isSalaryClient(scoringData.getIsSalaryClient())
            .paymentSchedule(paymentSchedule)
            .build();

    @Test
    void calculateCredit() {
        when(rateCalculator.calculateRate(false, false)).thenReturn(rate);
        when(rateCalculator.calculateFinalRate(scoringData, rate)).thenReturn(rate);
        when(annuityCalculator.calculateTotalAmount(amount, false)).thenReturn(amount);
        when(annuityCalculator.calculateMonthlyPayment(amount, term, rate)).thenReturn(monthlyPayment);
        when(annuityCalculator.calculatePaymentSchedule(amount, term, rate, monthlyPayment)).thenReturn(paymentSchedule);
        when(annuityCalculator.calculatePsk(paymentSchedule, amount, term)).thenReturn(psk);

        var creditDtoCurrent = creditService.calculateCredit(scoringData);

        assertEquals(credit, creditDtoCurrent);
        verify(scoringService, times(1)).scoring(scoringData);
        verify(rateCalculator, times(1)).calculateRate(false, false);
        verify(rateCalculator, times(1)).calculateFinalRate(scoringData, rate);
        verify(annuityCalculator, times(1)).calculateTotalAmount(amount, false);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amount, term, rate);
        verify(annuityCalculator, times(1)).calculatePaymentSchedule(amount, term, rate, monthlyPayment);
        verify(annuityCalculator, times(1)).calculatePsk(paymentSchedule, amount, term);
    }
}