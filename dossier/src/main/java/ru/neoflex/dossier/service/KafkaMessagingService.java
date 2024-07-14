package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.dossier.dto.EmailMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingService {

    private final EmailService emailService;

    @Transactional
    @KafkaListener(topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithFinishRegistration(@Payload EmailMessage emailMessage) {
        log.info("Message consumed {}", emailMessage);
        String text = "Ваша заявка предварительно одобрена, завершите оформление.";
        emailService.sendSimpleMessage(emailMessage.getAddress(), emailMessage.getTheme().toString(), text);
    }


    @Transactional
    @KafkaListener(topics = "create-documents",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithCreateDocuments(EmailMessage emailMessage) {
        log.info("Message consumed {}", emailMessage);
        String text = "Ваша заявка окончательно одобрена. [Получить документы по кредиту для подписания](ссылка)";
        emailService.sendSimpleMessage(emailMessage.getAddress(), emailMessage.getTheme().toString(), text);
    }
}
