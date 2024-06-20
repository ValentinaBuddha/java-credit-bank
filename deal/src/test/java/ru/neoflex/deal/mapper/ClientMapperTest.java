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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.EmploymentStatus.EMPLOYED;
import static ru.neoflex.deal.enums.Gender.FEMALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;
import static ru.neoflex.deal.enums.Position.TOP_MANAGER;

@SpringBootTest(classes = ru.neoflex.deal.mapper.ClientMapperImpl.class)
class ClientMapperTest {

    @Autowired
    private ClientMapper clientMapper;

    private final String lastName = "Ivanov";
    private final String firstName = "Ivan";
    private final String middleName = "Ivanovich";
    private final LocalDate birthdate = LocalDate.of(1980, 1, 1);
    private final String email = "ivan@gmail.com";
    private final Passport passport = new Passport(new PassportData("1234", "123456"));

    @Test
    void toClient() {
        final var loanStatement = LoanStatementRequestDto.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .birthdate(birthdate)
                .email(email)
                .build();

        final var mappedClient = clientMapper.toClient(loanStatement, passport);

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
        final var client = Client.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .birthdate(birthdate)
                .email(email)
                .passport(passport)
                .build();
        final var employmentData = new EmploymentData(EMPLOYED, "1234567890", BigDecimal.valueOf(300000),
                TOP_MANAGER, 256, 12);
        final var employment = new Employment(employmentData);
        final var finishRegistration = FinishRegistrationRequestDto.builder()
                .gender(FEMALE)
                .maritalStatus(MARRIED)
                .dependentAmount(1)
                .accountNumber("40817810100007408755")
                .build();

        final var mappedClient = clientMapper.toFullClient(client, finishRegistration, employment, passport);

        assertEquals(FEMALE, mappedClient.getGender());
        assertEquals(MARRIED, mappedClient.getMaritalStatus());
        assertEquals(1, mappedClient.getDependentAmount());
        assertEquals("40817810100007408755", mappedClient.getAccount());
        assertEquals(employment, mappedClient.getEmployment());
        assertEquals(passport, mappedClient.getPassport());
        assertNull(mappedClient.getId());
        assertEquals(lastName, mappedClient.getLastName());
        assertEquals(firstName, mappedClient.getFirstName());
        assertEquals(middleName, mappedClient.getMiddleName());
        assertEquals(birthdate, mappedClient.getBirthdate());
        assertEquals(email, mappedClient.getEmail());
    }
}