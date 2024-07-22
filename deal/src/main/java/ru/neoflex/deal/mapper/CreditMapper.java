package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.model.Credit;

@Mapper(componentModel = "spring", uses = {PaymentScheduleMapper.class})
public interface CreditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto credit);

    CreditDto toCreditDto(Credit credit);
}
