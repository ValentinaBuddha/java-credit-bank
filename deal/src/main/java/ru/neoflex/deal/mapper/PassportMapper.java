package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.deal.dto.PassportDto;
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

    @Mapping(target = "passportSeries", source = "passport.passportData.series")
    @Mapping(target = "passportNumber", source = "passport.passportData.number")
    @Mapping(target = "passportIssueDate", source = "passport.passportData.issueDate")
    @Mapping(target = "passportIssueBranch", source = "passport.passportData.issueBranch")
    PassportDto toPassportDto(Passport passport);
}
