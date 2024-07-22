package ru.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.dto.LoanStatementRequestWrapper;
import ru.neoflex.statement.feign.DealFeignClient;
import ru.neoflex.statement.service.PreskoringService;

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

    @MockBean
    private PreskoringService prescoringService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

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
    void calculateLoanOffers_whenValidData_thenReturnOk() throws Exception {
        when(dealFeignClient.calculateLoanOffers(new LoanStatementRequestWrapper())).thenReturn(List.of());

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateLoanOffers_whenAmountNull_thenExceptionThrows() throws Exception {
        loanStatement.setAmount(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
    void calculateLoanOffers_whenFirstNameNull_thenExceptionThrows() throws Exception {
        loanStatement.setFirstName(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenFirstNameBlank_thenExceptionThrows() throws Exception {
        loanStatement.setFirstName("");

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

    @Test
    void calculateLoanOffers_whenLastNameBlank_thenExceptionThrows() throws Exception {
        loanStatement.setLastName("");

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenEmailNull_thenExceptionThrows() throws Exception {
        loanStatement.setEmail(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenEmailBlank_thenExceptionThrows() throws Exception {
        loanStatement.setEmail("");

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenBirthdateNull_thenExceptionThrows() throws Exception {
        loanStatement.setBirthdate(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenPassportSeriesNull_thenExceptionThrows() throws Exception {
        loanStatement.setPassportSeries(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenPassportSeriesBlank_thenExceptionThrows() throws Exception {
        loanStatement.setPassportSeries("");

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenPassportNumberNull_thenExceptionThrows() throws Exception {
        loanStatement.setPassportNumber(null);

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateLoanOffers_whenPassportNumberBlank_thenExceptionThrows() throws Exception {
        loanStatement.setPassportNumber("");

        mvc.perform(post("/statement")
                        .content(mapper.writeValueAsString(loanStatement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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