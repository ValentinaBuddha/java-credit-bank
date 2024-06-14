package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.AppliedOffer;

public final class ScoringDataMapper {

    public static ScoringDataDto toDto(Statement statement, FinishRegistrationRequestDto finishRegistration) {

        AppliedOffer offer = statement.getAppliedOffer();
        Client client = statement.getClient();
        Passport passport = client.getPassport();

        return ScoringDataDto.builder()
                .amount(offer.getTotalAmount())
                .term(offer.getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistration.getGender())
                .birthdate(client.getBirthdate())
                .passportSeries(passport.getPassportData().getSeries())
                .passportNumber(passport.getPassportData().getNumber())
                .passportIssueDate(finishRegistration.getPassportIssueDate())
                .passportIssueBranch(finishRegistration.getPassportIssueBranch())
                .maritalStatus(finishRegistration.getMaritalStatus())
                .dependentAmount(finishRegistration.getDependentAmount())
                .employment(finishRegistration.getEmployment())
                .accountNumber(finishRegistration.getAccountNumber())
                .isInsuranceEnabled(offer.getIsInsuranceEnabled())
                .isSalaryClient(offer.getIsSalaryClient())
                .build();
    }

    private ScoringDataMapper() {
    }
}