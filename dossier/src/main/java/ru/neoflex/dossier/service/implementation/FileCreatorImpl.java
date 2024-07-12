package ru.neoflex.dossier.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.CreditDto;
import ru.neoflex.dossier.dto.PaymentScheduleElementDto;
import ru.neoflex.dossier.service.FileCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileCreatorImpl implements FileCreator {

    @Override
    public void createTxtFile(CreditDto creditDto) {

        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "IdeaProjects", "java-credit-bank", "dossier", "loan_documents.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toString()))) {

            writer.write("Credit Amount " + creditDto.getAmount() + "\n");
            writer.write("Term " + creditDto.getTerm() + "\n");
            writer.write("Monthly Payment " + creditDto.getMonthlyPayment() + "\n");
            writer.write("Rate " + creditDto.getRate() + "\n");
            writer.write("PSK " + creditDto.getPsk() + "\n");
            if (Boolean.TRUE.equals(creditDto.getIsInsuranceEnabled())) {
                writer.write("Insurance enabled\n");
            } else {
                writer.write("Insurance is not enabled\n");
            }
            if (Boolean.TRUE.equals(creditDto.getIsSalaryClient())) {
                writer.write("Salary Client - Yes\n\n");
            } else {
                writer.write("Salary Client - No\n\n");
            }

            writer.write("Number,Date,Total Payment,Interest Payment,Debt Payment,Remaining Debt\n");
            if (creditDto.getPaymentSchedule() != null) {
                for (PaymentScheduleElementDto payment : creditDto.getPaymentSchedule()) {
                    writer.write(payment.getNumber() + ",");
                    writer.write(payment.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ",");
                    writer.write(payment.getTotalPayment() + ",");
                    writer.write(payment.getInterestPayment() + ",");
                    writer.write(payment.getDebtPayment() + ",");
                    writer.write(payment.getRemainingDebt() + "\n");
                }
            }

            log.info("File created successfully.");

        } catch (IOException e) {
            log.error("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }
}
