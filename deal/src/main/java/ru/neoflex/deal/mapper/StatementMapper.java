package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.dto.StatementDtoFull;
import ru.neoflex.deal.dto.StatementDtoShort;
import ru.neoflex.deal.model.Statement;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CreditMapper.class, ClientMapper.class, OfferMapper.class})
public interface StatementMapper {

    StatementDtoFull toStatementDtoFull(Statement statement);

    List<StatementDtoShort> toListStatementDtoShort(List<Statement> statements);
}
