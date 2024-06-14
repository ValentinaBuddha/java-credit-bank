package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement;

public final class PaymentScheduleMapper {

    public static PaymentScheduleElement toEntity(PaymentScheduleElementDto element) {

        return new PaymentScheduleElement(
                element.getDate(),
                element.getTotalPayment(),
                element.getInterestPayment(),
                element.getDebtPayment(),
                element.getDebtPayment());
    }

    private PaymentScheduleMapper() {
    }
}
