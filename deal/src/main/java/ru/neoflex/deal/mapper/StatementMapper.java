package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.model.Statement;

@Mapper(componentModel = "spring", uses = {CreditMapper.class})
public interface StatementMapper {

    StatementDto toStatementDto(Statement statement);
}
