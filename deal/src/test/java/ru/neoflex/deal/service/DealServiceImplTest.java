package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.exception.EntityNotFoundException;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.EmploymentMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.PaymentScheduleMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.PassportData;
import ru.neoflex.deal.model.jsonb.StatementStatus;
import ru.neoflex.deal.reposiory.ClientRepository;
import ru.neoflex.deal.reposiory.CreditRepository;
import ru.neoflex.deal.reposiory.EmploymentRepository;
import ru.neoflex.deal.reposiory.PassportRepository;
import ru.neoflex.deal.reposiory.StatementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;
import static ru.neoflex.deal.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.deal.enums.Gender.MALE;
import static ru.neoflex.deal.enums.MaritalStatus.MARRIED;
import static ru.neoflex.deal.enums.Position.TOP_MANAGER;
import static ru.neoflex.deal.enums.Status.CC_DENIED;
import static ru.neoflex.deal.enums.Status.CLIENT_DENIED;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private StatementRepository statementRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private PassportRepository passportRepository;
    @Mock
    private EmploymentRepository employmentRepository;
    @Mock
    private CalculatorFeignClient calculatorFeignClient;
    @InjectMocks
    private DealServiceImpl dealService;

    private final BigDecimal amount = BigDecimal.valueOf(100000);
    private final BigDecimal amountWihInsurance = BigDecimal.valueOf(105000);
    private final int term = 6;
    private final String lastName = "Ivanov";
    private final String firstName = "Ivan";
    private final LocalDate birthdate = LocalDate.of(1980, 1, 1);
    private final String passportSeries = "1234";
    private final String passportNumber = "123456";
    private final String email = "ivan@gmail.com";
    private final UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private final LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
            .amount(amount)
            .term(term)
            .firstName(firstName)
            .lastName(lastName)
            .birthdate(birthdate)
            .passportSeries(passportSeries)
            .passportNumber(passportNumber)
            .email(email)
            .build();
    private final BigDecimal monthlyPayment1 = BigDecimal.valueOf(17602.27);
    private final BigDecimal monthlyPayment2 = BigDecimal.valueOf(17552.52);
    private final BigDecimal monthlyPayment3 = BigDecimal.valueOf(18325.68);
    private final BigDecimal monthlyPayment4 = BigDecimal.valueOf(18273.55);
    private final BigDecimal monthlyPayment = BigDecimal.valueOf(17205.46);
    private final BigDecimal rate1 = BigDecimal.valueOf(19);
    private final BigDecimal rate2 = BigDecimal.valueOf(18);
    private final BigDecimal rate3 = BigDecimal.valueOf(16);
    private final BigDecimal rate4 = BigDecimal.valueOf(15);
    private final BigDecimal rateAfterScoring = BigDecimal.valueOf(11);
    private final LoanOfferDto offer1 = LoanOfferDto.builder()
            .statementId(id)
            .totalAmount(amount)
            .monthlyPayment(monthlyPayment1)
            .rate(rate1)
            .build();
    private final LoanOfferDto offer2 = LoanOfferDto.builder()
            .statementId(id)
            .totalAmount(amount)
            .monthlyPayment(monthlyPayment2)
            .rate(rate2)
            .build();
    private final LoanOfferDto offer3 = LoanOfferDto.builder()
            .statementId(id)
            .totalAmount(amountWihInsurance)
            .monthlyPayment(monthlyPayment3)
            .rate(rate3)
            .build();
    private final LoanOfferDto offer4 = LoanOfferDto.builder()
            .statementId(id)
            .totalAmount(amountWihInsurance)
            .monthlyPayment(monthlyPayment4)
            .rate(rate4)
            .build();
    private final List<LoanOfferDto> offers = List.of(offer1, offer2, offer3, offer4);
    private final Passport passport = Passport.builder()
            .id(id)
            .passportData(new PassportData(passportSeries, passportNumber))
            .build();
    private final Client client = Client.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .birthdate(birthdate)
            .passport(passport)
            .email(email)
            .build();
    private final StatementStatus status = new StatementStatus(PREAPPROVAL, LocalDateTime.now(), AUTOMATIC);
    private final List<StatementStatus> history = new ArrayList<>(List.of(status));
    private final Statement statement = Statement.builder()
            .id(id)
            .client(client)
            .creationDate(LocalDateTime.now())
            .status(PREAPPROVAL)
            .statusHistory(history)
            .build();
    private final EmploymentDto employmentDto = EmploymentDto.builder()
            .employmentStatus(SELF_EMPLOYED)
            .employerINN("7707123456")
            .salary(BigDecimal.valueOf(70000))
            .position(TOP_MANAGER)
            .workExperienceTotal(256)
            .workExperienceCurrent(12)
            .build();
    private final FinishRegistrationRequestDto finishRegistration = FinishRegistrationRequestDto.builder()
            .gender(MALE)
            .maritalStatus(MARRIED)
            .dependentAmount(0)
            .passportIssueDate(LocalDate.of(1990,1,1))
            .passportIssueBranch("ОВД кировского района города Пензы")
            .employment(employmentDto)
            .accountNumber("40817810100007408755")
            .build();
    private final String incorrectId = "6dd2ff79-5597-4c58-9a88-55ab84c1111d";
    private final BigDecimal psk = BigDecimal.valueOf(6.465);
    private final LocalDate date = LocalDate.now();
    private final PaymentScheduleElementDto pse0 = new PaymentScheduleElementDto(0, date, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, amount);
    private final PaymentScheduleElementDto pse1 = new PaymentScheduleElementDto(1, date.plusMonths(1), monthlyPayment1,
            BigDecimal.valueOf(916.67), BigDecimal.valueOf(16288.79), BigDecimal.valueOf(83711.21));
    private final PaymentScheduleElementDto pse2 = new PaymentScheduleElementDto(2, date.plusMonths(2), monthlyPayment1,
            BigDecimal.valueOf(767.35), BigDecimal.valueOf(16438.11), BigDecimal.valueOf(67273.1));
    private final PaymentScheduleElementDto pse3 = new PaymentScheduleElementDto(3, date.plusMonths(2), monthlyPayment1,
            BigDecimal.valueOf(616.67), BigDecimal.valueOf(16588.79), BigDecimal.valueOf(50684.31));
    private final PaymentScheduleElementDto pse4 = new PaymentScheduleElementDto(4, date.plusMonths(4), monthlyPayment1,
            BigDecimal.valueOf(464.61), BigDecimal.valueOf(16740.85), BigDecimal.valueOf(33943.46));
    private final PaymentScheduleElementDto pse5 = new PaymentScheduleElementDto(5, date.plusMonths(5), monthlyPayment1,
            BigDecimal.valueOf(311.15), BigDecimal.valueOf(16894.31), BigDecimal.valueOf(17049.15));
    private final PaymentScheduleElementDto pse6 = new PaymentScheduleElementDto(6, date.plusMonths(6), monthlyPayment1,
            BigDecimal.valueOf(156.28), BigDecimal.valueOf(17049.15), BigDecimal.ZERO);
    private final List<PaymentScheduleElementDto> paymentSchedule = List.of(pse0, pse1, pse2, pse3, pse4, pse5, pse6);
    private final CreditDto creditDto = CreditDto.builder()
            .amount(amount)
            .term(term)
            .monthlyPayment(monthlyPayment)
            .rate(rateAfterScoring)
            .psk(psk)
            .paymentSchedule(paymentSchedule)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();

    @Test
    void calculateLoanOffers_whenClientDoesNotExist_thenClientSaved() {
        when(clientRepository.existsByEmail("ivan@gmail.com")).thenReturn(false);
        when(passportRepository.save(any())).thenReturn(passport);
        when(clientRepository.save(any())).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);
        when(calculatorFeignClient.getLoanOffers(loanStatement)).thenReturn(offers);

        final List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(loanStatement);

        verify(clientRepository, times(1)).existsByEmail(any());
        verify(passportRepository, times(1)).save(any());
        verify(clientRepository, times(1)).save(any());
        verify(statementRepository, times(1)).save(any());
        verify(calculatorFeignClient, times(1)).getLoanOffers(any());
        for (int i = 0; i < offers.size(); i++) {
            assertEquals(offers.get(i).getTotalAmount(), actualOffers.get(i).getTotalAmount());
            assertEquals(offers.get(i).getMonthlyPayment(), actualOffers.get(i).getMonthlyPayment());
            assertEquals(offers.get(i).getRate(), actualOffers.get(i).getRate());
            assertEquals(statement.getId(), actualOffers.get(i).getStatementId());
        }
    }

    @Test
    void calculateLoanOffers_whenClientExists_thenClientGet() {
        when(clientRepository.existsByEmail("ivan@gmail.com")).thenReturn(true);
        when(clientRepository.getClientByEmail(email)).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);
        when(calculatorFeignClient.getLoanOffers(loanStatement)).thenReturn(offers);

        final List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(loanStatement);

        verify(clientRepository, times(1)).existsByEmail(any());
        verify(clientRepository, times(1)).getClientByEmail(any());
        verify(statementRepository, times(1)).save(any());
        verify(calculatorFeignClient, times(1)).getLoanOffers(any());
        for (int i = 0; i < offers.size(); i++) {
            assertEquals(offers.get(i).getTotalAmount(), actualOffers.get(i).getTotalAmount());
            assertEquals(offers.get(i).getMonthlyPayment(), actualOffers.get(i).getMonthlyPayment());
            assertEquals(offers.get(i).getRate(), actualOffers.get(i).getRate());
            assertEquals(statement.getId(), actualOffers.get(i).getStatementId());
        }
    }

    @Test
    void selectLoanOffers_whenStatementFound_thenStatusAndOfferSaved() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        var appliedOffer = OfferMapper.toEntity(offer1);
        statement.setAppliedOffer(appliedOffer);

        dealService.selectLoanOffers(offer1);

        verify(statementRepository, times(2)).findById(any());
        assertEquals(CLIENT_DENIED, statement.getStatus());
        assertEquals(2, statement.getStatusHistory().size());
        assertEquals(CLIENT_DENIED, statement.getStatusHistory().get(1).getStatus());
        assertEquals(offer1.getStatementId(), appliedOffer.getStatementId());
        assertEquals(offer1.getRequestedAmount(), appliedOffer.getRequestedAmount());
        assertEquals(offer1.getTotalAmount(), appliedOffer.getTotalAmount());
        assertEquals(offer1.getTerm(), appliedOffer.getTerm());
        assertEquals(offer1.getMonthlyPayment(), appliedOffer.getMonthlyPayment());
        assertEquals(offer1.getRate(), appliedOffer.getRate());
        assertEquals(offer1.getIsInsuranceEnabled(), appliedOffer.getIsInsuranceEnabled());
        assertEquals(offer1.getIsSalaryClient(), appliedOffer.getIsSalaryClient());
    }

    @Test
    void selectLoanOffers_whenStatementNotFound_thenThrowsException() {
        offer1.setStatementId(UUID.fromString(incorrectId));
        when(statementRepository.findById(UUID.fromString(incorrectId))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dealService.selectLoanOffers(offer1));
    }

    @Test
    void finishRegistration_whenEntitiesFound_thenNoExceptions() {
        final var appliedOffer = OfferMapper.toEntity(offer1);
        statement.setAppliedOffer(appliedOffer);

        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        var scoringData = ScoringDataMapper.toDto(statement, finishRegistration);
        when(calculatorFeignClient.calculateCredit(any())).thenReturn(creditDto);
        var credit = CreditMapper.toEntity(creditDto);
        credit.setId(id);
        when(creditRepository.save(any())).thenReturn(credit);
        var employmentData = EmploymentMapper.toEntity(employmentDto);
        var employment = new Employment(id, employmentData);
        when(employmentRepository.save(any())).thenReturn(employment);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        ClientMapper.toFullEntity(client, finishRegistration, employment);

        dealService.finishRegistration(String.valueOf(id), finishRegistration);

        verify(statementRepository, times(1)).findById(any());
        verify(calculatorFeignClient, times(1)).calculateCredit(any());
        verify(employmentRepository, times(1)).save(any());
        verify(clientRepository, times(2)).findById(any());
        assertEquals(CC_DENIED, statement.getStatus());
        assertEquals(2, statement.getStatusHistory().size());
        assertEquals(CC_DENIED, statement.getStatusHistory().get(1).getStatus());
        assertEquals(amount, scoringData.getAmount());
        assertEquals(7, credit.getPaymentSchedule().size());
        for (int i = 0; i < 7; i++) {
            assertEquals(PaymentScheduleMapper.toEntity(paymentSchedule.get(i)), credit.getPaymentSchedule().get(i));
        }
        assertEquals(employment, client.getEmployment());
    }

    @Test
    void finishRegistration_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(UUID.fromString(incorrectId))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                dealService.finishRegistration(incorrectId, finishRegistration));
    }

    @Test
    void finishRegistration_whenClientNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        final var appliedOffer = OfferMapper.toEntity(offer1);
        statement.setAppliedOffer(appliedOffer);
        when(calculatorFeignClient.calculateCredit(any())).thenReturn(creditDto);
        final var credit = CreditMapper.toEntity(creditDto);
        when(creditRepository.save(any())).thenReturn(credit);
        final var employment = new Employment(id, EmploymentMapper.toEntity(employmentDto));
        when(employmentRepository.save(any())).thenReturn(employment);
        client.setId(UUID.fromString(incorrectId));
        statement.setClient(client);
        when(clientRepository.findById(UUID.fromString(incorrectId))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                dealService.finishRegistration(String.valueOf(id), finishRegistration));
    }
}