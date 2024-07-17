package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleMessage(String to, String subject, String text) {
        log.info("Send simple email");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            log.info("Message created");

            emailSender.send(message);
            log.info("Message sent");

        } catch (Exception exception) {
            log.info("Sending email exception: " + exception.getMessage());
        }
    }

    public void sendMessageWithAttachment(String to, String subject, String text) {
        log.info("Send email with attachment");

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            log.info("Message created");

            String userHome = System.getProperty("user.home");
            Path filePath = Paths.get(userHome, "IdeaProjects", "java-credit-bank", "dossier", "loan_documents.txt");
            File file = new File(filePath.toString());
            FileSystemResource documents = new FileSystemResource(file);
            helper.addAttachment(Objects.requireNonNull(documents.getFilename()), documents);
            log.info("Attachment added");

            emailSender.send(message);
            log.info("Message sent");

        } catch (Exception exception) {
            log.info("Sending email exception: " + exception.getMessage());
        }
    }
}