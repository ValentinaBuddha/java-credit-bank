package ru.neoflex.statement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.exception.PrescoringException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PreskoringServiceTest {

    private PreskoringService preskoringService = new PreskoringService();
    private LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(12)
            .firstName("Ivan")
            .lastName("Ivanov")
            .middleName("Ivanovich")
            .email("ivan@ya.ru")
            .birthdate(LocalDate.of(1990, 1, 1))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    @Test
    void prescoring_whenValidData_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenFirstNameValid_thenPreskoringOk(String firstName) {
        loanStatement.setFirstName(firstName);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenFirstNameNotValid_thenExceptionThrows(String firstName) {
        loanStatement.setFirstName(firstName);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenLastNameValid_thenPreskoringOk(String lastName) {
        loanStatement.setLastName(lastName);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenLastNameNotValid_thenExceptionThrows(String lastName) {
        loanStatement.setLastName(lastName);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenMiddleNameValid_thenPreskoringOk(String middleName) {
        loanStatement.setMiddleName(middleName);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void prescoring_whenMiddleNameNotValid_thenExceptionThrows(String middleName) {
        loanStatement.setMiddleName(middleName);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @Test
    void prescoring_whenAmountValid_thenPreskoringOk() {
        loanStatement.setAmount(BigDecimal.valueOf(30000));

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100000", "29999"})
    void prescoring_whenAmountNotValid_thenThrowsException(String amount) {
        loanStatement.setAmount(new BigDecimal(amount));

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void prescoring_whenTermValid_thenPreskoringOk(int term) {
        loanStatement.setTerm(term);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -12})
    void prescoring_whenTermNotValid_thenExceptionThrows(int term) {
        loanStatement.setTerm(term);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @Test
    void prescoring_whenAge18_thenPreskoringOk() {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18));

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @Test
    void prescoring_whenAgeMore18_thenPreskoringOk() {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18).minusDays(1));

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @Test
    void prescoring_whenAgeLess18_thenExceptionThrows() {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18).plusDays(1));

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"iv_an@ya.ru", "ivan@gmail.com", "ivan@neoflex.dev.com"})
    void prescoring_whenEmailValid_thenPreskoringOk(String email) {
        loanStatement.setEmail(email);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ivanya.ru", "шш@ya.ru", "ivan@шш.ru"})
    void prescoring_whenEmailNotValid_thenExceptionThrows(String email) {
        loanStatement.setEmail(email);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "0000"})
    void prescoring_whenPassportSeriesValid_thenPreskoringOk(String passportSeries) {
        loanStatement.setPassportSeries(passportSeries);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12 34", "123 ", "12345"})
    void prescoring_whenPassportSeriesNotValid_thenExceptionThrows(String passportSeries) {
        loanStatement.setPassportSeries(passportSeries);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "000000"})
    void prescoring_whenPassportNumberValid_thenPreskoringOk(String passportNumber) {
        loanStatement.setPassportNumber(passportNumber);

        assertDoesNotThrow(() -> preskoringService.prescoring(loanStatement));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12 3456", "12356 ", "1234567"})
    void prescoring_whenPassportNumberNotValid_thenExceptionThrows(String passportNumber) {
        loanStatement.setPassportNumber(passportNumber);

        PrescoringException exception = assertThrows(PrescoringException.class, () ->
                preskoringService.prescoring(loanStatement));

        assertEquals("Prescoring failed.", exception.getMessage());
    }
}
