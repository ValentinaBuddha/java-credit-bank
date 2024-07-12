package ru.neoflex.deal.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.service.KafkaMessagingService;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessagingServiceImpl implements KafkaMessagingService {

    private final KafkaTemplate<String , Object> kafkaTemplate;

    @Override
    public void sendMessage(String sendClientTopic, EmailMessage emailMessage) {
        kafkaTemplate.send(sendClientTopic, emailMessage.getStatementId(), emailMessage);
    }
}
