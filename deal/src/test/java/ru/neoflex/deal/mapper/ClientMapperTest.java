package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.jsonb.EmploymentData;
import ru.neoflex.deal.model.jsonb.PassportData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.EmploymentStatus.EMPLOYED;
import static ru.neoflex.deal.enums.Gender.FEMALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;
import static ru.neoflex.deal.enums.Position.TOP_MANAGER;

@SpringBootTest(classes = {ru.neoflex.deal.mapper.ClientMapperImpl.class,
        ru.neoflex.deal.mapper.PassportMapperImpl.class,
        ru.neoflex.deal.mapper.EmploymentMapperImpl.class})
class ClientMapperTest {

    @Autowired
    private ClientMapper clientMapper;

    private String lastName = "Ivanov";
    private String firstName = "Ivan";
    private String middleName = "Ivanovich";
    private LocalDate birthdate = LocalDate.of(1980, 1, 1);
    private String email = "ivan@gmail.com";
    private PassportData passportData = new PassportData("1234", "123456");
    private Passport passport = new Passport(passportData);

    private Client client = Client.builder()
            .lastName(lastName)
            .firstName(firstName)
            .middleName(middleName)
            .birthdate(birthdate)
            .email(email)
            .passport(passport)
            .build();
    private EmploymentData employmentData = new EmploymentData(EMPLOYED, "1234567890",
            BigDecimal.valueOf(300000), TOP_MANAGER, 256, 12);
    private Employment employment = new Employment(employmentData);
    private String issueBranch = "ОВД кировского района города Пензы";
    private LocalDate issueDate = LocalDate.of(1990,1,1);
    private FinishRegistrationRequestDto finishRegistration = FinishRegistrationRequestDto.builder()
            .gender(FEMALE)
            .maritalStatus(MARRIED)
            .dependentAmount(1)
            .accountNumber("40817810100007408755")
            .build();
    private UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");

    @Test
    void toClient() {
        var loanStatement = LoanStatementRequestDto.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .birthdate(birthdate)
                .email(email)
                .build();

        var mappedClient = clientMapper.toClient(loanStatement, passport);

        assertEquals(lastName, mappedClient.getLastName());
        assertEquals(firstName, mappedClient.getFirstName());
        assertEquals(middleName, mappedClient.getMiddleName());
        assertEquals(birthdate, mappedClient.getBirthdate());
        assertEquals(email, mappedClient.getEmail());
        assertEquals(passport, mappedClient.getPassport());
        assertNull(mappedClient.getId());
        assertNull(mappedClient.getGender());
        assertNull(mappedClient.getMaritalStatus());
        assertNull(mappedClient.getDependentAmount());
        assertNull(mappedClient.getEmployment());
        assertNull(mappedClient.getAccount());
    }

    @Test
    void toFullClient() {
        passportData.setIssueDate(issueDate);
        passportData.setIssueBranch(issueBranch);

        var mappedClient = clientMapper.toFullClient(client, finishRegistration, employment, passport);

        assertEquals(FEMALE, mappedClient.getGender());
        assertEquals(MARRIED, mappedClient.getMaritalStatus());
        assertEquals(1, mappedClient.getDependentAmount());
        assertEquals("40817810100007408755", mappedClient.getAccount());
        assertEquals(employment, mappedClient.getEmployment());
        assertEquals("1234", mappedClient.getPassport().getPassportData().getSeries());
        assertEquals("123456", mappedClient.getPassport().getPassportData().getNumber());
        assertEquals(issueBranch, mappedClient.getPassport().getPassportData().getIssueBranch());
        assertEquals(issueDate, mappedClient.getPassport().getPassportData().getIssueDate());
        assertNull(mappedClient.getId());
        assertEquals(lastName, mappedClient.getLastName());
        assertEquals(firstName, mappedClient.getFirstName());
        assertEquals(middleName, mappedClient.getMiddleName());
        assertEquals(birthdate, mappedClient.getBirthdate());
        assertEquals(email, mappedClient.getEmail());
    }

    @Test
    void toClientDtoFull() {
        client.setId(id);
        client.setGender(FEMALE);
        client.setMaritalStatus(MARRIED);
        client.setDependentAmount(1);
        client.setAccount("40817810100007408755");
        passport.setId(id);
        passportData.setIssueBranch(issueBranch);
        passportData.setIssueDate(issueDate);
        employment.setId(id);
        client.setEmployment(employment);

        var mappedClientDtoFull = clientMapper.toClientDtoFull(client);

        assertEquals(id, mappedClientDtoFull.getId());
        assertEquals(lastName, mappedClientDtoFull.getLastName());
        assertEquals(firstName, mappedClientDtoFull.getFirstName());
        assertEquals(middleName, mappedClientDtoFull.getMiddleName());
        assertEquals(birthdate, mappedClientDtoFull.getBirthdate());
        assertEquals(email, mappedClientDtoFull.getEmail());
        assertEquals(FEMALE, mappedClientDtoFull.getGender());
        assertEquals(MARRIED, mappedClientDtoFull.getMaritalStatus());
        assertEquals(1, mappedClientDtoFull.getDependentAmount());
        assertEquals("40817810100007408755", mappedClientDtoFull.getAccount());
        assertEquals(id, mappedClientDtoFull.getPassport().getId());
        assertEquals("1234", mappedClientDtoFull.getPassport().getPassportSeries());
        assertEquals("123456", mappedClientDtoFull.getPassport().getPassportNumber());
        assertEquals(issueBranch, mappedClientDtoFull.getPassport().getPassportIssueBranch());
        assertEquals(issueDate, mappedClientDtoFull.getPassport().getPassportIssueDate());
        assertEquals(id, mappedClientDtoFull.getEmployment().getId());
        assertEquals(EMPLOYED, mappedClientDtoFull.getEmployment().getEmploymentStatus());
        assertEquals("1234567890", mappedClientDtoFull.getEmployment().getEmployerInn());
        assertEquals(BigDecimal.valueOf(300000), mappedClientDtoFull.getEmployment().getSalary());
        assertEquals(TOP_MANAGER, mappedClientDtoFull.getEmployment().getPosition());
        assertEquals(256, mappedClientDtoFull.getEmployment().getWorkExperienceTotal());
        assertEquals(12, mappedClientDtoFull.getEmployment().getWorkExperienceCurrent());
    }

    @Test
    void toClientDtoShort() {
        client.setId(id);

        var mappedClientDtoShort = clientMapper.toClientDtoShort(client);

        assertEquals(id, mappedClientDtoShort.getId());
        assertEquals(lastName, mappedClientDtoShort.getLastName());
        assertEquals(firstName, mappedClientDtoShort.getFirstName());
        assertEquals(middleName, mappedClientDtoShort.getMiddleName());
    }
}