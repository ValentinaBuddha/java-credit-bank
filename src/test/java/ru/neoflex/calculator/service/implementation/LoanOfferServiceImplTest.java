package ru.neoflex.calculator.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.service.RateCalculatorService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOfferServiceImplTest {

    @Mock
    private AnnuityCalculatorServiceImpl annuityCalculator;

    @Mock
    private RateCalculatorService rateCalculator;

    @InjectMocks
    private LoanOfferServiceImpl loanOfferService;

    private final BigDecimal amount = BigDecimal.valueOf(100000);
    private final BigDecimal amountWihInsurance = BigDecimal.valueOf(105000);
    private final int term = 12;
    private final LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
            .amount(amount)
            .term(term)
            .build();
    private final BigDecimal monthlyPayment1 = BigDecimal.valueOf(9215.66);
    private final BigDecimal monthlyPayment2 = BigDecimal.valueOf(9526.74);
    private final BigDecimal monthlyPayment3 = BigDecimal.valueOf(9168);
    private final BigDecimal monthlyPayment4 = BigDecimal.valueOf(9477.12);
    private final BigDecimal rate1 = BigDecimal.valueOf(19);
    private final BigDecimal rate2 = BigDecimal.valueOf(16);
    private final BigDecimal rate3 = BigDecimal.valueOf(18);
    private final BigDecimal rate4 = BigDecimal.valueOf(15);

    @Test
    void calculateLoanOffers() {
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amount);
        when(rateCalculator.calculateRate(false, false)).thenReturn(rate1);
        when(annuityCalculator.calculateMonthlyPayment(amount, term, rate1)).thenReturn(monthlyPayment1);
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amountWihInsurance);
        when(rateCalculator.calculateRate(true, false)).thenReturn(rate2);
        when(annuityCalculator.calculateMonthlyPayment(amountWihInsurance, term, rate2)).thenReturn(monthlyPayment2);
        when(annuityCalculator.calculateTotalAmount(amount, true)).thenReturn(amountWihInsurance);
        when(rateCalculator.calculateRate(true, true)).thenReturn(rate4);
        when(annuityCalculator.calculateMonthlyPayment(amountWihInsurance, term, rate4)).thenReturn(monthlyPayment4);
        when(annuityCalculator.calculateTotalAmount(amount, false)).thenReturn(amount);
        when(rateCalculator.calculateRate(false, true)).thenReturn(rate3);
        when(annuityCalculator.calculateMonthlyPayment(amount, term, rate3)).thenReturn(monthlyPayment3);

        final List<LoanOfferDto> loanOffers = loanOfferService.calculateLoanOffers(loanStatement);

        assertEquals(4, loanOffers.size());
        assertEquals(rate1, loanOffers.get(0).getRate());
        assertEquals(rate3, loanOffers.get(1).getRate());
        assertEquals(rate2, loanOffers.get(2).getRate());
        assertEquals(rate4, loanOffers.get(3).getRate());
        assertEquals(amount, loanOffers.get(0).getTotalAmount());
        assertEquals(amount, loanOffers.get(1).getTotalAmount());
        assertEquals(amountWihInsurance, loanOffers.get(2).getTotalAmount());
        assertEquals(amountWihInsurance, loanOffers.get(3).getTotalAmount());
        assertEquals(monthlyPayment1, loanOffers.get(0).getMonthlyPayment());
        assertEquals(monthlyPayment3, loanOffers.get(1).getMonthlyPayment());
        assertEquals(monthlyPayment2, loanOffers.get(2).getMonthlyPayment());
        assertEquals(monthlyPayment4, loanOffers.get(3).getMonthlyPayment());
        verify(annuityCalculator, times(2)).calculateTotalAmount(amount, false);
        verify(annuityCalculator, times(2)).calculateTotalAmount(amount, true);
        verify(rateCalculator, times(1)).calculateRate(false, false);
        verify(rateCalculator, times(1)).calculateRate(true, false);
        verify(rateCalculator, times(1)).calculateRate(false, true);
        verify(rateCalculator, times(1)).calculateRate(true, true);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amount, term, rate1);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amountWihInsurance, term, rate2);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amount, term, rate3);
        verify(annuityCalculator, times(1)).calculateMonthlyPayment(amountWihInsurance, term, rate4);
    }
}