package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentScheduleMapper {

    List<PaymentScheduleElement> mapList(List<PaymentScheduleElementDto> paymentSchedule);
}
