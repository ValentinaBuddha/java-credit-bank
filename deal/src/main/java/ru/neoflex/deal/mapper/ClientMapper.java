package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Passport;

public final class ClientMapper {

    public static Client toEntity(LoanStatementRequestDto loanStatement, Passport passport) {

        return Client.builder()
                .lastName(loanStatement.getLastName())
                .firstName(loanStatement.getFirstName())
                .middleName(loanStatement.getMiddleName())
                .birthdate(loanStatement.getBirthdate())
                .email(loanStatement.getEmail())
                .passport(passport)
                .build();
    }

    public static void toFullEntity(Client client,
                                      FinishRegistrationRequestDto finishRegistration,
                                      Employment employment) {

        client.setGender(finishRegistration.getGender());
        client.setMaritalStatus(finishRegistration.getMaritalStatus());
        client.setDependentAmount(finishRegistration.getDependentAmount());
        client.setEmployment(employment);
        client.setAccount(finishRegistration.getAccountNumber());
    }

    private ClientMapper() {
    }
}
