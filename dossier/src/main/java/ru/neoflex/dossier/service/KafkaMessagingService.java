package ru.neoflex.dossier.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.dossier.dto.EmailMessage;

@Slf4j
@AllArgsConstructor
@Service
public class KafkaMessagingService {

    @Transactional
    @KafkaListener(topics = "finish-registration", groupId = "${spring.kafka.consumer.group-id}", properties = {"spring.json.value.default.type=ru.neoflex.dossier.dto.EmailMessage"})
    public void sendEmail(@Payload EmailMessage emailMessage) {
        log.info("кафка работает: {}", emailMessage);
    }
}
