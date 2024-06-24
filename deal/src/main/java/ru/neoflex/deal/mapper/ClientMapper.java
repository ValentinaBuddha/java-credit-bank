package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Passport;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastName", source = "loanStatement.lastName")
    @Mapping(target = "firstName", source = "loanStatement.firstName")
    @Mapping(target = "middleName", source = "loanStatement.middleName")
    @Mapping(target = "birthdate", source = "loanStatement.birthdate")
    @Mapping(target = "email", source = "loanStatement.email")
    @Mapping(target = "passport", source = "passport")
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "employment", ignore = true)
    @Mapping(target = "account", ignore = true)
    Client toClient(LoanStatementRequestDto loanStatement, Passport passport);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "middleName", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "gender", source = "finishRegistration.gender")
    @Mapping(target = "maritalStatus", source = "finishRegistration.maritalStatus")
    @Mapping(target = "dependentAmount", source = "finishRegistration.dependentAmount")
    @Mapping(target = "employment", source = "employment")
    @Mapping(target = "account", source = "finishRegistration.accountNumber")
    @Mapping(target = "passport", source = "passport")
    Client toFullClient(@MappingTarget Client client, FinishRegistrationRequestDto finishRegistration,
                      Employment employment, Passport passport);
}