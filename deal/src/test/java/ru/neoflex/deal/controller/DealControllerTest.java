package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestWrapper;
import ru.neoflex.deal.service.DealService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.neoflex.deal.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.deal.enums.Gender.MALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;
import static ru.neoflex.deal.enums.Position.TOP_MANAGER;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;

@WebMvcTest(controllers = DealController.class)
class DealControllerTest {

    @MockBean
    private DealService dealService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    EmploymentDto employmentDto = EmploymentDto.builder()
            .employmentStatus(SELF_EMPLOYED)
            .employerInn("7707123456")
            .salary(BigDecimal.valueOf(70000))
            .position(TOP_MANAGER)
            .workExperienceTotal(256)
            .workExperienceCurrent(12)
            .build();
    FinishRegistrationRequestDto finishRegistration = FinishRegistrationRequestDto.builder()
            .gender(MALE)
            .maritalStatus(MARRIED)
            .dependentAmount(0)
            .passportIssueDate(LocalDate.of(1990,1,1))
            .passportIssueBranch("ОВД кировского района города Пензы")
            .employment(employmentDto)
            .accountNumber("40817810100007408755")
            .build();

    @Test
    void calculateLoanOffers() throws Exception {
        when(dealService.calculateLoanOffers(new LoanStatementRequestDto(), PREAPPROVAL)).thenReturn(List.of());

        mvc.perform(post("/deal/statement")
                        .content(mapper.writeValueAsString(new LoanStatementRequestWrapper()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void selectLoanOffers() throws Exception {
        mvc.perform(post("/deal/offer/select")
                        .content(mapper.writeValueAsString(new LoanOfferDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenValidData_thenReturnOk() throws Exception {
        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenGenderNull_thenExceptionThrows() throws Exception {
        finishRegistration.setGender(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenMaritalStatusNull_thenExceptionThrows() throws Exception {
        finishRegistration.setMaritalStatus(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenDependentAmountNull_thenExceptionThrows() throws Exception {
        finishRegistration.setDependentAmount(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenDependentAmountNegative_thenExceptionThrows() throws Exception {
        finishRegistration.setDependentAmount(-1);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenPassportIssueDateNotValid_thenExceptionThrows() throws Exception {
        finishRegistration.setPassportIssueDate(LocalDate.now().plusDays(1));

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenPassportIssueDateToday_thenReturnOk() throws Exception {
        finishRegistration.setPassportIssueDate(LocalDate.now());

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenPassportIssueDateYesterday_thenReturnOk() throws Exception {
        finishRegistration.setPassportIssueDate(LocalDate.now().minusDays(1));

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenPassportIssueBranchNull_thenExceptionThrows() throws Exception {
        finishRegistration.setPassportIssueBranch(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "4081781010000740875 5", " 40817810100007408755"})
    void calculateLoanOffers_whenAccountNumberNotValid_thenExceptionThrows(String accountNumber) throws Exception {
        finishRegistration.setAccountNumber(accountNumber);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenEmploymentNull_thenExceptionThrows() throws Exception {
        finishRegistration.setEmployment(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenEmploymentStatusNull_thenExceptionThrows() throws Exception {
        employmentDto.setEmploymentStatus(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenEmployerInnNull_thenExceptionThrows() throws Exception {
        employmentDto.setEmployerInn(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "770712345 ", "770712345 6", "77071234567", "7707123456789"})
    void calculateLoanOffers_whenEmployerInnNotValid_thenExceptionThrows(String employerInn) throws Exception {
        employmentDto.setEmployerInn(employerInn);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenEmployerInnValid_thenReturnOk() throws Exception {
        employmentDto.setEmployerInn("770712345678");

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenSalaryNull_thenExceptionThrows() throws Exception {
        employmentDto.setSalary(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenSalaryNegative_thenExceptionThrows() throws Exception {
        employmentDto.setSalary(BigDecimal.valueOf(-1));

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    void calculateLoanOffers_whenSalaryValid_thenReturnOk(String salary) throws Exception {
        employmentDto.setSalary(new BigDecimal(salary));

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenPositionNull_thenExceptionThrows() throws Exception {
        employmentDto.setPosition(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenWorkExperienceTotalNull_thenExceptionThrows() throws Exception {
        employmentDto.setWorkExperienceTotal(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenWorkExperienceTotalNegative_thenExceptionThrows() throws Exception {
        employmentDto.setWorkExperienceTotal(-1);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void calculateLoanOffers_whenWorkExperienceTotalValid_thenReturnOk(int workExperienceTotal) throws Exception {
        employmentDto.setWorkExperienceTotal(workExperienceTotal);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void calculateCredit_whenWorkExperienceCurrentNull_thenExceptionThrows() throws Exception {
        employmentDto.setWorkExperienceCurrent(null);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateCredit_whenWorkExperienceCurrentNegative_thenExceptionThrows() throws Exception {
        employmentDto.setWorkExperienceCurrent(-1);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void calculateLoanOffers_whenWorkExperienceCurrentValid_thenReturnOk(int workExperienceTotal) throws Exception {
        employmentDto.setWorkExperienceCurrent(workExperienceTotal);

        mvc.perform(post("/deal/calculate/6dd2ff79-5597-4c58-9a88-55ab84c9378d")
                        .content(mapper.writeValueAsString(finishRegistration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
