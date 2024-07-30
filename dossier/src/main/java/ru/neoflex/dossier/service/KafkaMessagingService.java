package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.feign.DealFeignClient;

import static ru.neoflex.dossier.enums.Status.DOCUMENT_CREATED;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingService {

    private final EmailService emailService;
    private final DealFeignClient dealFeignClient;
    private static final String MESSAGE_CONSUMED = "Message consumed {}";

    @KafkaListener(topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithFinishRegistration(@Payload EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        var text = "Ваша заявка предварительно одобрена, завершите оформление.";
        emailService.sendSimpleMessage(emailMessage.getAddress(), "Завершите оформление", text);
    }

    @KafkaListener(topics = "create-documents",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithCreateDocuments(EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        var text = "Ваша заявка окончательно одобрена.\n[Сформировать документы.](ссылка)";
        emailService.sendSimpleMessage(emailMessage.getAddress(), "Заявка на кредит одобрена", text);
    }

    @KafkaListener(topics = "send-documents",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithSendDocuments(EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        dealFeignClient.saveStatementStatus(emailMessage.getStatementId(), DOCUMENT_CREATED);

        var statement = dealFeignClient.findStatementById(emailMessage.getStatementId());
        var credit = statement.getCredit();

        var text = "Документы по кредиту.\n[Запрос на согласие с условиями.](ссылка)";
        emailService.sendMessageWithAttachment(emailMessage.getAddress(), "Документы по кредиту", text, credit);
    }

    @KafkaListener(topics = "send-ses",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithSendSes(EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        var statementDto = dealFeignClient.findStatementById(emailMessage.getStatementId());
        String sesCode = statementDto.getSesCode();

        var text = "Код подтверждения " + sesCode + ".\n[Подписать документы.](ссылка)";
        emailService.sendSimpleMessage(emailMessage.getAddress(), "Подпишите документы по кредиту", text);
    }

    @KafkaListener(topics = "credit-issued",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithCreditIssued(EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        var text = "Кредит выдан.";
        emailService.sendSimpleMessage(emailMessage.getAddress(), "Кредит выдан", text);
    }

    @KafkaListener(topics = "statement-denied",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithStatementDenied(EmailMessage emailMessage) {
        log.info(MESSAGE_CONSUMED, emailMessage);

        var text = "Заявка на кредит отклонена.\nОбратитесь в отделение банка за дополнительной информацией.";
        emailService.sendSimpleMessage(emailMessage.getAddress(), "Заявка на кредит отклонена", text);
    }
}