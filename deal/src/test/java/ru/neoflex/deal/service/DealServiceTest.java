package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.exception.ScoringException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private final UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private final Client client = Client.builder()
            .passport(new Passport(new PassportData()))
            .email("ivan@gmail.com")
            .build();
    private final Statement statement = Statement.builder().id(id).client(client).build();
    private final FinishRegistrationRequestDto finishRegistration = new FinishRegistrationRequestDto();

    @Test
    void calculateLoanOffers() {
        final List<LoanOfferDto> offers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());
        when(clientService.saveClient(any())).thenReturn(client);
        when(statementRepository.save(any())).thenReturn(statement);
        when(calculatorFeignClient.calculateLoanOffers(any())).thenReturn(offers);

        final List<LoanOfferDto> actualOffers = dealService.calculateLoanOffers(new LoanStatementRequestDto());

        verify(clientService, times(1)).saveClient(any());
        verify(statementRepository, times(1)).save(any());
        verify(calculatorFeignClient, times(1)).calculateLoanOffers(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any());
        for (int i = 0; i < offers.size(); i++) {
            assertEquals(statement.getId(), actualOffers.get(i).getStatementId());
        }
    }

    @Test
    void selectLoanOffers_whenStatementFound_thenStatusAndOfferSaved() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(offerMapper.toAppliedOffer(any())).thenReturn(new AppliedOffer());

        assertDoesNotThrow(() -> dealService.selectLoanOffers(new LoanOfferDto()));

        verify(statementRepository, times(2)).findById(any());
        verify(offerMapper, times(1)).toAppliedOffer(any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any());
    }

    @Test
    void selectLoanOffers_whenStatementNotFound_thenThrowsException() {
        final var loanOfferDto = LoanOfferDto.builder().statementId(id).build();
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        final EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dealService.selectLoanOffers(loanOfferDto));

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
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any());
        verify(clientService, times(1)).finishRegistration(any(), any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
    }

    @Test
    void finishRegistration_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        final EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                dealService.finishRegistration(String.valueOf(id), finishRegistration));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void finishRegistration_whenScoringFailed_thenStatementDenied() {
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));
        when(scoringDataMapper.toScoringDataDto(any(), any(), any(), any())).thenReturn(new ScoringDataDto());
        when(calculatorFeignClient.calculateCredit(any())).thenThrow(new ScoringException("message"));

        dealService.finishRegistration(String.valueOf(id), finishRegistration);

        verify(statementRepository, times(1)).findById(any());
        verify(scoringDataMapper, times(1)).toScoringDataDto(any(), any(), any(), any());
        verify(calculatorFeignClient, times(1)).calculateCredit(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any());
    }
}