package ru.neoflex.statement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.exception.PrescoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.neoflex.statement.util.StringPatterns.EMAIL;
import static ru.neoflex.statement.util.StringPatterns.LATIN_ALPHABET;
import static ru.neoflex.statement.util.StringPatterns.PASSPORT_NUMBER;
import static ru.neoflex.statement.util.StringPatterns.PASSPORT_SERIES;

@Slf4j
@Service
public class PreskoringService {

    public void prescoring(LoanStatementRequestDto loanStatement) {
        log.info("Prescoring: loan statement = {}", loanStatement);

        List<String> reasonsForRefusal = new ArrayList<>();

        if (!loanStatement.getFirstName().matches(LATIN_ALPHABET)) {
            reasonsForRefusal.add("Имя должно содержать от 2 до 30 латинских букв");
        }
        if (!loanStatement.getLastName().matches(LATIN_ALPHABET)) {
            reasonsForRefusal.add("Фамилия должна содержать от 2 до 30 латинских букв");
        }
        if (loanStatement.getMiddleName() != null && !loanStatement.getMiddleName().matches(LATIN_ALPHABET)) {
            reasonsForRefusal.add("Отчество должно содержать от 2 до 30 латинских букв");
        }
        if (loanStatement.getAmount().compareTo(BigDecimal.valueOf(30000)) < 0) {
            reasonsForRefusal.add("Сумма кредита должна быть больше или равна 30 000.00 рублей");
        }
        if (loanStatement.getTerm() < 6) {
            reasonsForRefusal.add("Срок кредита должен быть больше или равен 6ти месяцам");
        }
        var date = LocalDate.now().minusYears(18);
        if (loanStatement.getBirthdate().isAfter(date)) {
            reasonsForRefusal.add("Кредит выдаётся лицам, достигшим 18-ти лет");
        }
        if (!loanStatement.getEmail().matches(EMAIL)) {
            reasonsForRefusal.add("Поле email имеет некорректный формат");
        }
        if (!loanStatement.getPassportSeries().matches(PASSPORT_SERIES)) {
            reasonsForRefusal.add("Серия паспорта должна содержать 4 цифры");
        }
        if (!loanStatement.getPassportNumber().matches(PASSPORT_NUMBER)) {
            reasonsForRefusal.add("Номер паспорта должен содержать 6 цифр");
        }

        if (!reasonsForRefusal.isEmpty()) {
            log.info(String.format("Prescoring result - rejection. Reasons: %s",
                    String.join(", ", reasonsForRefusal)));
            throw new PrescoringException("Prescoring failed.");
        } else {
            log.info("Prescoring was successful!");
        }
    }
}