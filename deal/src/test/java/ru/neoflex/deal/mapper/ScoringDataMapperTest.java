package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.jsonb.AppliedOffer;
import ru.neoflex.deal.model.jsonb.PassportData;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.neoflex.deal.enums.EmploymentStatus.EMPLOYED;
import static ru.neoflex.deal.enums.Gender.FEMALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;
import static ru.neoflex.deal.enums.Position.TOP_MANAGER;

@SpringBootTest(classes = ru.neoflex.deal.mapper.ScoringDataMapperImpl.class)
class ScoringDataMapperTest {

    @Autowired
    private ScoringDataMapper scoringDataMapper;

    @Test
    void toScoringDataDto() {
        final var employmentDto = new EmploymentDto(EMPLOYED, "1234567890", BigDecimal.valueOf(300000),
                TOP_MANAGER, 256, 12);
        final var finishRegistration = FinishRegistrationRequestDto.builder()
                .gender(FEMALE)
                .maritalStatus(MARRIED)
                .dependentAmount(1)
                .accountNumber("40817810100007408755")
                .passportIssueBranch("ОВД")
                .passportIssueDate(LocalDate.of(1990, 1, 1))
                .employment(employmentDto)
                .build();
        final var offer = AppliedOffer.builder()
                .totalAmount(BigDecimal.valueOf(100000))
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
        final var client = Client.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthdate(LocalDate.of(1980, 1, 1))
                .build();
        final var passportData = new PassportData("1234", "123456");

        final var mappedScoringDataDto = scoringDataMapper.toScoringDataDto(finishRegistration, offer, client, passportData);

        assertEquals(offer.getTotalAmount(), mappedScoringDataDto.getAmount());
        assertEquals(offer.getTerm(), mappedScoringDataDto.getTerm());
        assertEquals(client.getLastName(), mappedScoringDataDto.getLastName());
        assertEquals(client.getFirstName(), mappedScoringDataDto.getFirstName());
        assertEquals(client.getMiddleName(), mappedScoringDataDto.getMiddleName());
        assertEquals(finishRegistration.getGender(), mappedScoringDataDto.getGender());
        assertEquals(client.getBirthdate(), mappedScoringDataDto.getBirthdate());
        assertEquals(passportData.getSeries(), mappedScoringDataDto.getPassportSeries());
        assertEquals(passportData.getNumber(), mappedScoringDataDto.getPassportNumber());
        assertEquals(finishRegistration.getPassportIssueDate(), mappedScoringDataDto.getPassportIssueDate());
        assertEquals(finishRegistration.getPassportIssueBranch(), mappedScoringDataDto.getPassportIssueBranch());
        assertEquals(finishRegistration.getMaritalStatus(), mappedScoringDataDto.getMaritalStatus());
        assertEquals(finishRegistration.getDependentAmount(), mappedScoringDataDto.getDependentAmount());
        assertEquals(employmentDto, mappedScoringDataDto.getEmployment());
        assertEquals(finishRegistration.getAccountNumber(), mappedScoringDataDto.getAccountNumber());
        assertEquals(offer.getIsInsuranceEnabled(), mappedScoringDataDto.getIsInsuranceEnabled());
        assertEquals(offer.getIsSalaryClient(), mappedScoringDataDto.getIsSalaryClient());
    }
}