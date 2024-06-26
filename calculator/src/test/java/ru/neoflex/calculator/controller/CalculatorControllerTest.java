package ru.neoflex.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOfferService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.neoflex.calculator.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.calculator.enums.Gender.MALE;
import static ru.neoflex.calculator.enums.MaritalStatus.MARRIED;
import static ru.neoflex.calculator.enums.Position.TOP_MANAGER;

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
        final var loanStatement = LoanStatementRequestDto.builder()
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

        when(loanOfferService.calculateLoanOffers(loanStatement)).thenReturn(List.of());


        mvc.perform(post("/calculator/offers")
                        .content(mapper.writeValueAsString(loanStatement))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit() throws Exception {
        final var employment = EmploymentDto.builder()
                .employmentStatus(SELF_EMPLOYED)
                .employerINN("1234567890")
                .salary(BigDecimal.valueOf(300000))
                .position(TOP_MANAGER)
                .workExperienceTotal(19)
                .workExperienceCurrent(4)
                .build();
        final var scoringData = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .gender(MALE)
                .birthdate(LocalDate.of(1986, 11, 16))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2014, 11, 16))
                .passportIssueBranch("UVD")
                .maritalStatus(MARRIED)
                .dependentAmount(0)
                .employment(employment)
                .accountNumber("40817810100007408755")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        when(creditService.calculateCredit(scoringData)).thenReturn(any());

        mvc.perform(post("/calculator/calc")
                        .content(mapper.writeValueAsString(scoringData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}