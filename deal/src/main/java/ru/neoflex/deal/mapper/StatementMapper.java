package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.dto.StatementDtoForDossier;
import ru.neoflex.deal.dto.StatementDtoShort;
import ru.neoflex.deal.model.Statement;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CreditMapper.class, ClientMapper.class})
public interface StatementMapper {

    StatementDtoForDossier toStatementCreditDto(Statement statement);

    List<StatementDtoShort> toListStatementDtoShort(List<Statement> statements);
}
