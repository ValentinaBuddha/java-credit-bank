package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.enums.Theme;
import ru.neoflex.deal.exception.VerifySesCodeException;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.reposiory.StatementRepository;
import ru.neoflex.deal.util.SesCodeGenerator;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;
import static ru.neoflex.deal.enums.CreditStatus.ISSUED;
import static ru.neoflex.deal.enums.Status.DOCUMENT_SIGNED;
import static ru.neoflex.deal.enums.Status.PREPARE_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.SEND_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.SEND_SES;

/**
 * Service for signing documents.
 *
 * @author Valentina Vakhlamova
 */
@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final StatementRepository statementRepository;
    private final AdminService adminService;
    private final KafkaMessagingService kafkaMessagingService;

    public void sendDocuments(String statementId) {
        log.info("Create kafka message to send documents for statementId = {}", statementId);

        var statement = findStatementById(UUID.fromString(statementId));

        adminService.saveStatementStatus(statement, PREPARE_DOCUMENTS, AUTOMATIC);

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(SEND_DOCUMENTS)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("send-documents", emailMessage);
    }

    public void signDocuments(String statementId) {
        log.info("Create kafka message to send sesCode for statementId = {}", statementId);

        var statement = findStatementById(UUID.fromString(statementId));

        int sesCode = SesCodeGenerator.generateSesCode();
        statement.setSesCode(String.valueOf(sesCode));
        log.info("SesCode generated = {} and saved to statement", sesCode);

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(SEND_SES)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("send-ses", emailMessage);
    }

    public void verifySesCode(String statementId, String sesCode) {
        log.info("Create kafka message to verify sesCode = {} and issue credit for statementId = {}", sesCode, statementId);

        var statement = findStatementById(UUID.fromString(statementId));

        if (!statement.getSesCode().equals(sesCode)) {
            log.info("Ses code is invalid.");
            throw new VerifySesCodeException("Ses code is invalid.");
        }

        adminService.saveStatementStatus(statement, DOCUMENT_SIGNED, AUTOMATIC);
        log.info("Credit documents signed.");
        statement.setSignDate(LocalDateTime.now());
        statement.getCredit().setCreditStatus(ISSUED);
        adminService.saveStatementStatus(statement, Status.CREDIT_ISSUED, AUTOMATIC);
        log.info("Credit issued.");

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(Theme.CREDIT_ISSUED)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("credit-issued", emailMessage);
    }

    private Statement findStatementById(UUID statementId) {
        var statement = statementRepository.findById(statementId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", statementId)));
        log.info("Statement found = {}", statement);
        return statement;
    }
}