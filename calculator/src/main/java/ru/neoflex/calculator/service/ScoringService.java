package ru.neoflex.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Scoring.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@Service
public class ScoringService {

    public void scoring(ScoringDataDto scoringData) {
        log.info("Scoring: scoringData = {}", scoringData);

        List<String> reasonsForRefusal = new ArrayList<>();
        EmploymentDto employment = scoringData.getEmployment();
        var sixtyFiveYearsAgo = LocalDate.now().minusYears(65);
        var twentyYearsAgo = LocalDate.now().minusYears(20);

        if (employment.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            reasonsForRefusal.add("Working status unemployed");
        }

        if (scoringData.getAmount().compareTo(employment.getSalary().multiply(BigDecimal.valueOf(25))) > 0) {
            reasonsForRefusal.add("The loan amount is more than 25 salaries");
        }

        if (scoringData.getBirthdate().isBefore(sixtyFiveYearsAgo)) {
            reasonsForRefusal.add("Age over 65 years");
        } else if (scoringData.getBirthdate().isAfter(twentyYearsAgo)) {
            reasonsForRefusal.add("Age less than 20 years");
        }

        if (employment.getWorkExperienceTotal() < 18) {
            reasonsForRefusal.add("Total experience less than 18 months");
        }

        if (employment.getWorkExperienceCurrent() < 3) {
            reasonsForRefusal.add("Current experience less than 3 months");
        }

        if (!reasonsForRefusal.isEmpty()) {
            throw new ScoringException(String.format("Scoring result - rejection. Reasons: %s",
                    String.join(", ", reasonsForRefusal)));
        } else {
            log.info("Scoring was successful!");
        }
    }
}