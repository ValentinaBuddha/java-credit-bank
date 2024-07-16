package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

import static ru.neoflex.deal.enums.CreditStatus.ISSUED;
import static ru.neoflex.deal.enums.Status.DOCUMENT_SIGNED;
import static ru.neoflex.deal.enums.Status.PREPARE_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.SEND_DOCUMENTS;
import static ru.neoflex.deal.enums.Theme.SEND_SES;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final StatementRepository statementRepository;
    private final StatementService statementService;
    private final KafkaMessagingService kafkaMessagingService;
    private static final String SENT_TO_KAFKA = "Message sent to Kafka = {}";
    private static final String STATEMENT_FOUND = "Statement found = {}";

    public void sendDocuments(String statementId) {
        var statement = findStatementById(UUID.fromString(statementId));
        log.info(STATEMENT_FOUND, statement);

        statementService.saveStatementStatus(statement, PREPARE_DOCUMENTS);

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(SEND_DOCUMENTS)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("send-documents", emailMessage);
        log.info(SENT_TO_KAFKA, emailMessage);
    }

    public void signDocuments(String statementId) {
        var statement = findStatementById(UUID.fromString(statementId));
        log.info(STATEMENT_FOUND, statement);

        int sesCode = SesCodeGenerator.generateSesCode();
        statement.setSesCode(String.valueOf(sesCode));

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(SEND_SES)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("send-ses", emailMessage);
        log.info(SENT_TO_KAFKA, emailMessage);
    }

    public void verifySesCode(String statementId, String sesCode) {
        var statement = findStatementById(UUID.fromString(statementId));
        log.info(STATEMENT_FOUND, statement);

        if (!statement.getSesCode().equals(sesCode)) {
            throw new VerifySesCodeException("Ses code is invalid.");
        }

        statementService.saveStatementStatus(statementId, DOCUMENT_SIGNED);
        statement.setSignDate(LocalDateTime.now());
        statement.getCredit().setCreditStatus(ISSUED);
        statementService.saveStatementStatus(statementId, Status.CREDIT_ISSUED);

        var emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(Theme.CREDIT_ISSUED)
                .statementId(statementId)
                .build();
        kafkaMessagingService.sendMessage("credit-issued", emailMessage);
        log.info(SENT_TO_KAFKA, emailMessage);
    }

    private Statement findStatementById(UUID statementId) {
        return statementRepository.findById(statementId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", statementId)));
    }
}