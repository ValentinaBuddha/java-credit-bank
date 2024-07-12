package ru.neoflex.dossier.service;

import ru.neoflex.dossier.dto.EmailMessage;

public interface KafkaMessagingService {

    void sendEmailWithFinishRegistration(EmailMessage emailMessage);
}
