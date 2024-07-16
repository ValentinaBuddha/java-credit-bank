package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.CreditStatus.CALCULATED;

@SpringBootTest(classes = {ru.neoflex.deal.mapper.CreditMapperImpl.class,
        ru.neoflex.deal.mapper.PaymentScheduleMapperImpl.class})
class CreditMapperTest {

    @Autowired
    private CreditMapper creditMapper;

    private final BigDecimal amount = BigDecimal.valueOf(100000);
    private final Integer term = 6;
    private final BigDecimal psk = BigDecimal.valueOf(6.465);
    private final BigDecimal monthlyPayment = BigDecimal.valueOf(17205.46);
    private final LocalDate date = LocalDate.now();
    private final BigDecimal rate = BigDecimal.valueOf(11);

    private final PaymentScheduleElementDto pseDto0 = new PaymentScheduleElementDto(0, date, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, amount);
    private final PaymentScheduleElementDto pseDto1 = new PaymentScheduleElementDto(1, date.plusMonths(1),
            monthlyPayment, BigDecimal.valueOf(916.67), BigDecimal.valueOf(16288.79), BigDecimal.valueOf(83711.21));
    private final PaymentScheduleElementDto pseDto2 = new PaymentScheduleElementDto(2, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(767.35), BigDecimal.valueOf(16438.11), BigDecimal.valueOf(67273.1));
    private final PaymentScheduleElementDto pseDto3 = new PaymentScheduleElementDto(3, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(616.67), BigDecimal.valueOf(16588.79), BigDecimal.valueOf(50684.31));
    private final PaymentScheduleElementDto pseDto4 = new PaymentScheduleElementDto(4, date.plusMonths(4),
            monthlyPayment, BigDecimal.valueOf(464.61), BigDecimal.valueOf(16740.85), BigDecimal.valueOf(33943.46));
    private final PaymentScheduleElementDto pseDto5 = new PaymentScheduleElementDto(5, date.plusMonths(5),
            monthlyPayment, BigDecimal.valueOf(311.15), BigDecimal.valueOf(16894.31), BigDecimal.valueOf(17049.15));
    private final PaymentScheduleElementDto pseDto6 = new PaymentScheduleElementDto(6, date.plusMonths(6),
            monthlyPayment, BigDecimal.valueOf(156.28), BigDecimal.valueOf(17049.15), BigDecimal.ZERO);

    private final List<PaymentScheduleElementDto> paymentScheduleDtos = List.of(pseDto0, pseDto1, pseDto2, pseDto3, pseDto4,
            pseDto5, pseDto6);

    private final PaymentScheduleElement pse0 = new PaymentScheduleElement(0, date, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, amount);
    private final PaymentScheduleElement pse1 = new PaymentScheduleElement(1, date.plusMonths(1),
            monthlyPayment, BigDecimal.valueOf(916.67), BigDecimal.valueOf(16288.79), BigDecimal.valueOf(83711.21));
    private final PaymentScheduleElement pse2 = new PaymentScheduleElement(2, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(767.35), BigDecimal.valueOf(16438.11), BigDecimal.valueOf(67273.1));
    private final PaymentScheduleElement pse3 = new PaymentScheduleElement(3, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(616.67), BigDecimal.valueOf(16588.79), BigDecimal.valueOf(50684.31));
    private final PaymentScheduleElement pse4 = new PaymentScheduleElement(4, date.plusMonths(4),
            monthlyPayment, BigDecimal.valueOf(464.61), BigDecimal.valueOf(16740.85), BigDecimal.valueOf(33943.46));
    private final PaymentScheduleElement pse5 = new PaymentScheduleElement(5, date.plusMonths(5),
            monthlyPayment, BigDecimal.valueOf(311.15), BigDecimal.valueOf(16894.31), BigDecimal.valueOf(17049.15));
    private final PaymentScheduleElement pse6 = new PaymentScheduleElement(6, date.plusMonths(6),
            monthlyPayment, BigDecimal.valueOf(156.28), BigDecimal.valueOf(17049.15), BigDecimal.ZERO);

    private final List<PaymentScheduleElement> paymentSchedule = List.of(pse0, pse1, pse2, pse3, pse4, pse5, pse6);

    private final CreditDto creditDto = new CreditDto(amount, term, monthlyPayment, rate, psk, false,
            false, paymentScheduleDtos);
    private final Credit credit = new Credit(null, amount, term, monthlyPayment, rate, psk, paymentSchedule,
            false, false, CALCULATED);

    @Test
    void toCredit() {
        final var mappedCredit = creditMapper.toCredit(creditDto);

        assertEquals(amount, mappedCredit.getAmount());
        assertEquals(term, mappedCredit.getTerm());
        assertEquals(monthlyPayment, mappedCredit.getMonthlyPayment());
        assertEquals(rate, mappedCredit.getRate());
        assertEquals(psk, mappedCredit.getPsk());
        assertEquals(paymentSchedule, mappedCredit.getPaymentSchedule());
        assertEquals(false, mappedCredit.getIsInsuranceEnabled());
        assertEquals(false, mappedCredit.getIsSalaryClient());
        assertEquals(CALCULATED, mappedCredit.getCreditStatus());
        assertNull(mappedCredit.getId());
        assertEquals(7, mappedCredit.getPaymentSchedule().size());
        for (int i = 0; i < 7; i++) {
            assertEquals(paymentSchedule.get(i), mappedCredit.getPaymentSchedule().get(i));
        }
    }

    @Test
    void toCreditDto() {
        final var mappedCreditDto = creditMapper.toCreditDto(credit);

        assertEquals(amount, mappedCreditDto.getAmount());
        assertEquals(term, mappedCreditDto.getTerm());
        assertEquals(monthlyPayment, mappedCreditDto.getMonthlyPayment());
        assertEquals(rate, mappedCreditDto.getRate());
        assertEquals(psk, mappedCreditDto.getPsk());
        assertEquals(paymentScheduleDtos, mappedCreditDto.getPaymentSchedule());
        assertEquals(false, mappedCreditDto.getIsInsuranceEnabled());
        assertEquals(false, mappedCreditDto.getIsSalaryClient());
        assertEquals(7, mappedCreditDto.getPaymentSchedule().size());
        for (int i = 0; i < 7; i++) {
            assertEquals(paymentScheduleDtos.get(i), mappedCreditDto.getPaymentSchedule().get(i));
        }
    }
}