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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.CreditStatus.CALCULATED;

@SpringBootTest(classes = {ru.neoflex.deal.mapper.CreditMapperImpl.class,
        ru.neoflex.deal.mapper.PaymentScheduleMapperImpl.class})
class CreditMapperTest {

    @Autowired
    private CreditMapper creditMapper;

    private BigDecimal amount = BigDecimal.valueOf(100000);
    private Integer term = 6;
    private BigDecimal psk = BigDecimal.valueOf(6.465);
    private BigDecimal monthlyPayment = BigDecimal.valueOf(17205.46);
    private LocalDate date = LocalDate.now();
    private BigDecimal rate = BigDecimal.valueOf(11);

    private PaymentScheduleElementDto pseDto0 = new PaymentScheduleElementDto(0, date, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, amount);
    private PaymentScheduleElementDto pseDto1 = new PaymentScheduleElementDto(1, date.plusMonths(1),
            monthlyPayment, BigDecimal.valueOf(916.67), BigDecimal.valueOf(16288.79), BigDecimal.valueOf(83711.21));
    private PaymentScheduleElementDto pseDto2 = new PaymentScheduleElementDto(2, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(767.35), BigDecimal.valueOf(16438.11), BigDecimal.valueOf(67273.1));
    private PaymentScheduleElementDto pseDto3 = new PaymentScheduleElementDto(3, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(616.67), BigDecimal.valueOf(16588.79), BigDecimal.valueOf(50684.31));
    private PaymentScheduleElementDto pseDto4 = new PaymentScheduleElementDto(4, date.plusMonths(4),
            monthlyPayment, BigDecimal.valueOf(464.61), BigDecimal.valueOf(16740.85), BigDecimal.valueOf(33943.46));
    private PaymentScheduleElementDto pseDto5 = new PaymentScheduleElementDto(5, date.plusMonths(5),
            monthlyPayment, BigDecimal.valueOf(311.15), BigDecimal.valueOf(16894.31), BigDecimal.valueOf(17049.15));
    private PaymentScheduleElementDto pseDto6 = new PaymentScheduleElementDto(6, date.plusMonths(6),
            monthlyPayment, BigDecimal.valueOf(156.28), BigDecimal.valueOf(17049.15), BigDecimal.ZERO);

    private List<PaymentScheduleElementDto> paymentScheduleDtos = List.of(pseDto0, pseDto1, pseDto2, pseDto3, pseDto4,
            pseDto5, pseDto6);

    private PaymentScheduleElement pse0 = new PaymentScheduleElement(0, date, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, amount);
    private PaymentScheduleElement pse1 = new PaymentScheduleElement(1, date.plusMonths(1),
            monthlyPayment, BigDecimal.valueOf(916.67), BigDecimal.valueOf(16288.79), BigDecimal.valueOf(83711.21));
    private PaymentScheduleElement pse2 = new PaymentScheduleElement(2, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(767.35), BigDecimal.valueOf(16438.11), BigDecimal.valueOf(67273.1));
    private PaymentScheduleElement pse3 = new PaymentScheduleElement(3, date.plusMonths(2),
            monthlyPayment, BigDecimal.valueOf(616.67), BigDecimal.valueOf(16588.79), BigDecimal.valueOf(50684.31));
    private PaymentScheduleElement pse4 = new PaymentScheduleElement(4, date.plusMonths(4),
            monthlyPayment, BigDecimal.valueOf(464.61), BigDecimal.valueOf(16740.85), BigDecimal.valueOf(33943.46));
    private PaymentScheduleElement pse5 = new PaymentScheduleElement(5, date.plusMonths(5),
            monthlyPayment, BigDecimal.valueOf(311.15), BigDecimal.valueOf(16894.31), BigDecimal.valueOf(17049.15));
    private PaymentScheduleElement pse6 = new PaymentScheduleElement(6, date.plusMonths(6),
            monthlyPayment, BigDecimal.valueOf(156.28), BigDecimal.valueOf(17049.15), BigDecimal.ZERO);

    private List<PaymentScheduleElement> paymentSchedule = List.of(pse0, pse1, pse2, pse3, pse4, pse5, pse6);

    private CreditDto creditDto = new CreditDto(amount, term, monthlyPayment, rate, psk, false,
            false, paymentScheduleDtos);
    private Credit credit = new Credit(null, amount, term, monthlyPayment, rate, psk, paymentSchedule,
            false, false, CALCULATED);
    private UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");

    @Test
    void toCredit() {
        var mappedCredit = creditMapper.toCredit(creditDto);

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
    void toCreditDtoFull() {
        credit.setId(id);
        credit.setCreditStatus(CALCULATED);

        var mappedCreditDtoFull = creditMapper.toCreditDtoFull(credit);

        assertEquals(id, mappedCreditDtoFull.getId());
        assertEquals(amount, mappedCreditDtoFull.getAmount());
        assertEquals(term, mappedCreditDtoFull.getTerm());
        assertEquals(monthlyPayment, mappedCreditDtoFull.getMonthlyPayment());
        assertEquals(rate, mappedCreditDtoFull.getRate());
        assertEquals(psk, mappedCreditDtoFull.getPsk());
        assertEquals(paymentScheduleDtos, mappedCreditDtoFull.getPaymentSchedule());
        assertEquals(false, mappedCreditDtoFull.getIsInsuranceEnabled());
        assertEquals(false, mappedCreditDtoFull.getIsSalaryClient());
        assertEquals(CALCULATED, mappedCreditDtoFull.getCreditStatus());
        assertEquals(7, mappedCreditDtoFull.getPaymentSchedule().size());
        for (int i = 0; i < 7; i++) {
            assertEquals(paymentScheduleDtos.get(i), mappedCreditDtoFull.getPaymentSchedule().get(i));
        }
    }

    @Test
    void toCreditDtoShort() {
        credit.setId(id);

        var mappedCreditDtoShort = creditMapper.toCreditDtoShort(credit);

        assertEquals(id, mappedCreditDtoShort.getId());
        assertEquals(amount, mappedCreditDtoShort.getAmount());
        assertEquals(term, mappedCreditDtoShort.getTerm());
        assertEquals(monthlyPayment, mappedCreditDtoShort.getMonthlyPayment());
        assertEquals(rate, mappedCreditDtoShort.getRate());
        assertEquals(psk, mappedCreditDtoShort.getPsk());
        assertEquals(false, mappedCreditDtoShort.getIsInsuranceEnabled());
        assertEquals(false, mappedCreditDtoShort.getIsSalaryClient());
        assertEquals(CALCULATED, mappedCreditDtoShort.getCreditStatus());
    }
}
