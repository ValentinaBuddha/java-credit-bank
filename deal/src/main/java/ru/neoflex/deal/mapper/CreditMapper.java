package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement;

import java.util.List;

import static ru.neoflex.deal.enums.CreditStatus.CALCULATED;

public final class CreditMapper {
    public static Credit toEntity(CreditDto credit) {
        List<PaymentScheduleElement> schedule = credit.getPaymentSchedule().stream()
                .map(PaymentScheduleMapper::toEntity)
                .toList();
        return Credit.builder()
                .amount(credit.getAmount())
                .term(credit.getTerm())
                .monthlyPayment(credit.getMonthlyPayment())
                .rate(credit.getRate())
                .psk(credit.getPsk())
                .paymentSchedule(schedule)
                .isInsuranceEnable(credit.getIsInsuranceEnabled())
                .isSalaryClient(credit.getIsSalaryClient())
                .creditStatus(CALCULATED)
                .build();
    }

    private CreditMapper() {
    }
}
