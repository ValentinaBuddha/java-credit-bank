package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.model.Statement;

@Mapper(componentModel = "spring")
public interface StatementMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "appliedOffer", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    StatementDto toDto(Statement statement);
}
