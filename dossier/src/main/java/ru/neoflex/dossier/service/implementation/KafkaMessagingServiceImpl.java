package ru.neoflex.dossier.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.service.EmailService;
import ru.neoflex.dossier.service.KafkaMessagingService;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingServiceImpl implements KafkaMessagingService {

    private final EmailService emailService;

    @Transactional
    @KafkaListener(topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmailWithFinishRegistration(@Payload EmailMessage emailMessage) {
        log.info("Message consumed {}", emailMessage);
        String text = "Ваша заявка предварительно одобрена, завершите оформление";
        emailService.sendSimpleMessage(emailMessage.getAddress(), emailMessage.getTheme().toString(), text);
    }
}
