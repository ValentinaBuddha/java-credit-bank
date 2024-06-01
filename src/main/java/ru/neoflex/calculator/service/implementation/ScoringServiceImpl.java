package ru.neoflex.calculator.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.dto.enums.EmploymentStatus;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Scoring.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@Service
public class ScoringServiceImpl implements ScoringService {

    @Override
    public void scoring(ScoringDataDto scoringData) {
        log.info("Scoring: scoringData = {}", scoringData);

        EmploymentDto employment = scoringData.getEmployment();
        LocalDate sixtyFiveYearsAgo = LocalDate.now().minusYears(65);
        LocalDate twentyYearsAgo = LocalDate.now().minusYears(20);

        if (employment.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new ScoringException("Rejection. Working status unemployed.");
        }

        if (scoringData.getAmount().compareTo(employment.getSalary().multiply(BigDecimal.valueOf(25))) > 0) {
            throw new ScoringException("Rejection. The loan amount is more than 25 salaries.");
        }

        if (scoringData.getBirthdate().isBefore(sixtyFiveYearsAgo)) {
            throw new ScoringException("Rejection. Age over 65 years.");
        } else if (scoringData.getBirthdate().isAfter(twentyYearsAgo)) {
            throw new ScoringException("Rejection. Age less than 20 years.");
        }

        if (employment.getWorkExperienceTotal() < 18) {
            throw new ScoringException("Rejection. Total experience less than 18 months.");
        }

        if (employment.getWorkExperienceCurrent() < 3) {
            throw new ScoringException("Rejection. Current experience less than 3 months.");
        }
    }
}
