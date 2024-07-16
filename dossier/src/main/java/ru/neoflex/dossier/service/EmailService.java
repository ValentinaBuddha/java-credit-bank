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
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error("Sending email exception: " + exception.getMessage());
        }
    }

    public void sendMessageWithAttachment(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            String userHome = System.getProperty("user.home");
            Path filePath = Paths.get(userHome, "IdeaProjects", "java-credit-bank", "dossier", "loan_documents.txt");
            File file = new File(filePath.toString());
            FileSystemResource documents = new FileSystemResource(file);
            helper.addAttachment(Objects.requireNonNull(documents.getFilename()), documents);

            emailSender.send(message);
        } catch (Exception exception) {
            log.error("Sending email exception: " + exception.getMessage());
        }
    }
}