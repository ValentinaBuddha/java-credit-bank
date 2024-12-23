package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.exception.BadRequestException;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.AppliedOffer;
import ru.neoflex.deal.model.jsonb.PassportData;
import ru.neoflex.deal.reposiory.CreditRepository;
import ru.neoflex.deal.reposiory.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.neoflex.deal.enums.Status.CLIENT_DENIED;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private StatementRepository statementRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private AdminService adminService;
    @Mock
    private CalculatorFeignClient calculatorFeignClient;
    @Mock
    private KafkaMessagingService kafkaMessagingService;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private CreditMapper creditMapper;
    @Mock
    private ScoringDataMapper scoringDataMapper;
    @InjectMocks
    private DealService dealService;

    private UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private Client client = Client.builder()
            .passport(new Passport(new PassportData()))
            .email("ivan@gmail.com")
            .build();
    private Statement statement = Statement.builder().id(id).client(client).build();
    private FinishRegistrationRequestDto finishRegistration = new FinishRegistrationRequestDto();

    @Test
    void calculateLoanOffers_whenStatusPreapproval() {
        List<LoanOfferDto> offers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());
        when(clientService.saveClient(any())).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);
        when(calculatorFeignClient.calculateLoanOffers(any())).thenReturn(offers);

        List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(new LoanStatementRequestDto(), PREAPPROVAL);

        verify(clientService, times(1)).saveClient(any());
        verify(statementRepository, times(1)).save(any());
        verify(calculatorFeignClient, times(1)).calculateLoanOffers(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
        for (int i = 0; i < offers.size(); i++) {
            assertEquals(statement.getId(), actualOffers.get(i).getStatementId());
        }
    }

    @Test
    void calculateLoanOffers_whenStatusClientDenied() {
        when(clientService.saveClient(any())).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);

        List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(new LoanStatementRequestDto(), CLIENT_DENIED);

        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
        verify(clientService, times(1)).saveClient(any());
        verify(statementRepository, times(1)).save(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
        assertTrue(actualOffers.isEmpty());
    }

    @Test
    void selectLoanOffer_whenStatementFound_thenStatusAndOfferSaved() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(offerMapper.toAppliedOffer(any())).thenReturn(new AppliedOffer());

        assertDoesNotThrow(() -> dealService.selectLoanOffer(new LoanOfferDto()));

        verify(statementRepository, times(1)).findById(any());
        verify(offerMapper, times(1)).toAppliedOffer(any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
    }

    @Test
    void selectLoanOffer_whenStatementNotFound_thenThrowsException() {
        var loanOfferDto = LoanOfferDto.builder().statementId(id).build();
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dealService.selectLoanOffer(loanOfferDto));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
    }

    @Test
    void finishRegistration_whenEntitiesFound_thenNoExceptions() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(scoringDataMapper.toScoringDataDto(any(), any(), any(), any())).thenReturn(new ScoringDataDto());
        when(calculatorFeignClient.calculateCredit(any())).thenReturn(new CreditDto());
        when(creditMapper.toCredit(any())).thenReturn(new Credit());
        when(creditRepository.save(any())).thenReturn(new Credit());

        assertDoesNotThrow(() -> dealService.finishRegistration(String.valueOf(id), finishRegistration));

        verify(statementRepository, times(1)).findById(any());
        verify(scoringDataMapper, times(1)).toScoringDataDto(any(), any(), any(), any());
        verify(calculatorFeignClient, times(1)).calculateCredit(any());
        verify(creditMapper, times(1)).toCredit(any());
        verify(creditRepository, times(1)).save(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
        verify(clientService, times(1)).finishRegistration(any(), any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
    }

    @Test
    void finishRegistration_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        Executable executable = () -> dealService.finishRegistration(String.valueOf(id), finishRegistration);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, executable);

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void finishRegistration_whenScoringFailed_thenStatementDenied() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(scoringDataMapper.toScoringDataDto(any(), any(), any(), any())).thenReturn(new ScoringDataDto());
        when(calculatorFeignClient.calculateCredit(any())).thenThrow(new BadRequestException("message"));

        dealService.finishRegistration(String.valueOf(id), finishRegistration);

        verify(statementRepository, times(1)).findById(any());
        verify(scoringDataMapper, times(1)).toScoringDataDto(any(), any(), any(), any());
        verify(calculatorFeignClient, times(1)).calculateCredit(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
    }
}
