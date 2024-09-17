package ru.neoflex.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOfferService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CalculatorController.class)
class CalculatorControllerTest {

    @MockBean
    private LoanOfferService loanOfferService;

    @MockBean
    private CreditService creditService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void calculateLoanOffers() throws Exception {
        when(loanOfferService.calculateLoanOffers(new LoanStatementRequestDto())).thenReturn(List.of());

        mvc.perform(post("/calculator/offers")
                        .content(mapper.writeValueAsString(new LoanStatementRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit() throws Exception {
        when(creditService.calculateCredit(new ScoringDataDto())).thenReturn(any());

        mvc.perform(post("/calculator/calc")
                        .content(mapper.writeValueAsString(new ScoringDataDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
