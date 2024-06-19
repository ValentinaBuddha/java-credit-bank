package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.jsonb.PassportData;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passportData", source = "passportData")
    Passport toPassport(PassportData passportData);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passportData", source = "passportData")
    Passport toFullPassport(@MappingTarget Passport passport, PassportData passportData);
}
