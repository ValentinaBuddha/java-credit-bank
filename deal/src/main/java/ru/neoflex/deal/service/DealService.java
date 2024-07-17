package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.exception.ScoringException;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.reposiory.CreditRepository;
import ru.neoflex.deal.reposiory.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.neoflex.deal.enums.Status.APPROVED;
import static ru.neoflex.deal.enums.Status.CC_APPROVED;
import static ru.neoflex.deal.enums.Status.CC_DENIED;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;
import static ru.neoflex.deal.enums.Theme.CREATE_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.FINISH_REGISTRATION;
import static ru.neoflex.deal.enums.Theme.STATEMENT_DENIED;

/**
 * Service for credit parameters calculation and saving data.
 *
 * @author Valentina Vakhlamova
 */
@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class DealService {

    private final CalculatorFeignClient calculatorFeignClient;
    private final KafkaMessagingService kafkaMessagingService;
    private final ClientService clientService;
    private final AdminService adminService;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final ScoringDataMapper scoringDataMapper;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Calculate loan offers in dealService: loanStatement = {}", loanStatement);

        var client = clientService.saveClient(loanStatement);

        var statement = new Statement(client, LocalDateTime.now(), new ArrayList<>());
        var savedStatement = statementRepository.save(statement);
        log.info("Statement saved = {}", savedStatement);

        adminService.saveStatementStatus(savedStatement, PREAPPROVAL);

        List<LoanOfferDto> offers = calculatorFeignClient.calculateLoanOffers(loanStatement);
        offers.forEach(offer -> offer.setStatementId(savedStatement.getId()));
        log.info("Offers get from CalculatorMS: {}", offers.stream()
                .map(LoanOfferDto::toString)
                .collect(Collectors.joining(", ")));

        return offers;
    }

    public void selectLoanOffers(LoanOfferDto loanOffer) {
        log.info("Select one loan offer in dealService = {}", loanOffer);

        var id = loanOffer.getStatementId();
        var statement = findStatementById(id);

        adminService.saveStatementStatus(statement, APPROVED);

        var appliedOffer = offerMapper.toAppliedOffer(loanOffer);
        statement.setAppliedOffer(appliedOffer);
        log.info("Statement with selected offer saved = {}", findStatementById(id));

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(FINISH_REGISTRATION)
                .statementId(String.valueOf(id))
                .build();
        kafkaMessagingService.sendMessage("finish-registration", emailMessage);
    }

    public void finishRegistration(String statementId, FinishRegistrationRequestDto finishRegistration) {
        log.info("Finish registration: statementId = {}, finishRegistration = {}", statementId, finishRegistration);

        var statement = findStatementById(UUID.fromString(statementId));

        var offer = statement.getAppliedOffer();
        var client = statement.getClient();
        var passportData = client.getPassport().getPassportData();
        var scoringData = scoringDataMapper.toScoringDataDto(finishRegistration, offer, client, passportData);
        log.info("ScoringData prepared = {}", scoringData);

        CreditDto creditDto = null;
        try {
            creditDto = calculatorFeignClient.calculateCredit(scoringData);
            log.info("CreditDto get from CalculatorMS = {}", creditDto);

        } catch (ScoringException exception) {
            log.info(exception.getMessage());

            adminService.saveStatementStatus(statement, CC_DENIED);

            var emailMessage = EmailMessage.builder()
                    .address(statement.getClient().getEmail())
                    .theme(STATEMENT_DENIED)
                    .statementId(statementId)
                    .build();
            kafkaMessagingService.sendMessage("statement-denied", emailMessage);
        }

        if (creditDto != null) {
            var credit = creditMapper.toCredit(creditDto);
            var savedCredit = creditRepository.save(credit);
            statement.setCredit(savedCredit);
            log.info("Credit saved = {}", savedCredit);

            adminService.saveStatementStatus(statement, CC_APPROVED);

            clientService.finishRegistration(client, finishRegistration);

            var emailMessage = EmailMessage.builder()
                    .address(statement.getClient().getEmail())
                    .theme(CREATE_DOCUMENTS)
                    .statementId(statementId)
                    .build();
            kafkaMessagingService.sendMessage("create-documents", emailMessage);
        }
    }

    private Statement findStatementById(UUID statementId) {
        var statement = statementRepository.findById(statementId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", statementId)));
        log.info("Statement found = {}", statement);
        return statement;
    }
}