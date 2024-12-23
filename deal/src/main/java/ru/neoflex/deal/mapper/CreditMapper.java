package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.CreditDtoShort;
import ru.neoflex.deal.dto.CreditDtoFull;
import ru.neoflex.deal.model.Credit;

@Mapper(componentModel = "spring", uses = {PaymentScheduleMapper.class})
public interface CreditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto credit);

    CreditDtoFull toCreditDtoFull(Credit credit);

    CreditDtoShort toCreditDtoShort(Credit credit);
}
