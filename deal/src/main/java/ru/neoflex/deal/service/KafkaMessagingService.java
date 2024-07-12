package ru.neoflex.deal.service;

import ru.neoflex.deal.dto.EmailMessage;

public interface KafkaMessagingService {

    void sendMessage(String sendClientTopic, EmailMessage emailMessage);
}
