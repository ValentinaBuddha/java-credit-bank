package ru.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.feign.DealFeignClient;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatementController.class)
class StatementControllerTest {

    @MockBean
    private DealFeignClient dealFeignClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
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
    void calculateLoanOffers_whenValidData_thenPreskoringOk() throws Exception {
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenFirstNameValid_thenPreskoringOk(String firstName) throws Exception {
        loanStatement.setFirstName(firstName);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenFirstNameNotValid_thenExceptionThrows(String firstName) throws Exception {
        loanStatement.setFirstName(firstName);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenFirstNameNull_thenExceptionThrows() throws Exception {
        loanStatement.setFirstName(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenLastNameValid_thenPreskoringOk(String lastName) throws Exception {
        loanStatement.setLastName(lastName);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenLastNameNotValid_thenExceptionThrows(String lastName) throws Exception {
        loanStatement.setLastName(lastName);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenLastNameNull_thenExceptionThrows() throws Exception {
        loanStatement.setLastName(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SS", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenMiddleNameValid_thenPreskoringOk(String middleName) throws Exception {
        loanStatement.setMiddleName(middleName);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"S", "ШШ", "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"})
    void calculateLoanOffers_whenMiddleNameNotValid_thenExceptionThrows(String middleName) throws Exception {
        loanStatement.setMiddleName(middleName);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100000", "29999"})
    void calculateLoanOffers_whenAmountNotValid_thenExceptionThrows(String amount) throws Exception {
        loanStatement.setAmount(new BigDecimal(amount));

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenAmountValid_thenPreskoringOk() throws Exception {
        loanStatement.setAmount(BigDecimal.valueOf(30000));
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenAmountNull_thenExceptionThrows() throws Exception {
        loanStatement.setEmail(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -12})
    void calculateLoanOffers_whenTermNotValid_thenExceptionThrows(int term) throws Exception {
        loanStatement.setTerm(term);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7})
    void calculateLoanOffers_whenTermValid_thenPreskoringOk(int term) throws Exception {
        loanStatement.setTerm(term);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenTermNull_thenExceptionThrows() throws Exception {
        loanStatement.setTerm(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenAgeLess18_thenExceptionThrows() throws Exception {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18).plusDays(1));

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenAge18_thenPreskoringOk() throws Exception {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18));
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenAgeMore18_thenPreskoringOk() throws Exception {
        loanStatement.setBirthdate(LocalDate.now().minusYears(18).minusDays(1));
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenBirthdateNull_thenExceptionThrows() throws Exception {
        loanStatement.setBirthdate(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ivanya.ru", "шш@ya.ru", "ivan@шш.ru"})
    void calculateLoanOffers_whenEmailNotValid_thenExceptionThrows(String email) throws Exception {
        loanStatement.setEmail(email);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"iv_an@ya.ru", "ivan@gmail.com", "ivan@neoflex.dev.com"})
    void calculateLoanOffers_whenEmailValid_thenPreskoringOk(String email) throws Exception {
        loanStatement.setEmail(email);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenEmailNull_thenExceptionThrows() throws Exception {
        loanStatement.setEmail(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12 34", "123 ", "12345"})
    void calculateLoanOffers_whenPassportSeriesNotValid_thenExceptionThrows(String passportSeries) throws Exception {
        loanStatement.setPassportSeries(passportSeries);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "0000"})
    void calculateLoanOffers_whenPassportSeriesValid_thenPreskoringOk(String passportSeries) throws Exception {
        loanStatement.setPassportSeries(passportSeries);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12 3456", "12356 ", "1234567"})
    void calculateLoanOffers_whenPassportNumberNotValid_thenExceptionThrows(String passportNumber) throws Exception {
        loanStatement.setPassportNumber(passportNumber);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "000000"})
    void calculateLoanOffers_whenPassportNumberValid_thenPreskoringOk(String passportNumber) throws Exception {
        loanStatement.setPassportNumber(passportNumber);
        when(dealFeignClient.calculateLoanOffers(loanStatement)).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void selectLoanOffers() throws Exception {
               mvc.perform(post("/statement/offer")
                        .content(mapper.writeValueAsString(new LoanOfferDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}