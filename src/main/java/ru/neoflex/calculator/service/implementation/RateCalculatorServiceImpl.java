package ru.neoflex.calculator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.ApplicationConfig;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.dto.enums.Gender;
import ru.neoflex.calculator.dto.enums.MaritalStatus;
import ru.neoflex.calculator.service.RateCalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.calculator.dto.enums.EmploymentStatus.BUSINESS_OWNER;
import static ru.neoflex.calculator.dto.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.calculator.dto.enums.Gender.*;
import static ru.neoflex.calculator.dto.enums.MaritalStatus.DIVORCED;
import static ru.neoflex.calculator.dto.enums.MaritalStatus.MARRIED;
import static ru.neoflex.calculator.dto.enums.Position.MIDDLE_MANAGER;
import static ru.neoflex.calculator.dto.enums.Position.TOP_MANAGER;
import static ru.neoflex.calculator.util.BigDecimalConstant.*;

/**
 * Calculate credit parameters.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RateCalculatorServiceImpl implements RateCalculatorService {

    private final ApplicationConfig applicationConfig;

    @Override
    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("Calculating rate: isInsuranceEnabled = {}, isSalaryClient = {}",
                isInsuranceEnabled, isSalaryClient);

        BigDecimal rate = applicationConfig.getRate();

        if (isInsuranceEnabled) {
            rate = rate.subtract(THREE);
        }
        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.ONE);
        }

        return rate;
    }

    @Override
    public BigDecimal calculateFinalRate(ScoringDataDto scoringData, BigDecimal previousRate) {
        log.info("Final calculating rate: scoringData = {}, rate = {}", scoringData, previousRate);

        BigDecimal rate = previousRate;
        EmploymentDto employment = scoringData.getEmployment();
        Gender gender = scoringData.getGender();
        LocalDate birthday = scoringData.getBirthdate();
        MaritalStatus maritalStatus = scoringData.getMaritalStatus();

        if (employment.getEmploymentStatus() == SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.ONE);
        }

        if (employment.getEmploymentStatus() == BUSINESS_OWNER) {
            rate = rate.add(TWO);
        }

        if (employment.getPosition() == MIDDLE_MANAGER) {
            rate = rate.subtract(TWO);
        }

        if (employment.getPosition() == TOP_MANAGER) {
            rate = rate.subtract(THREE);
        }

        if (maritalStatus == MARRIED) {
            rate = rate.subtract(THREE);
        }

        if (maritalStatus == DIVORCED) {
            rate = rate.add(BigDecimal.ONE);
        }

        if (gender == NON_BINARY) {
            rate = rate.add(SEVEN);
        }

        if (gender == FEMALE) {
            if (isOlder(32, birthday) && isYounger(60, birthday)) {
                rate = rate.subtract(THREE);
            }
        }

        if (gender == MALE) {
            if (isOlder(30, birthday) && isYounger(55, birthday)) {
                rate = rate.subtract(THREE);
            }
        }

        return rate;
    }

    private boolean isOlder(int minAge, LocalDate birthday) {
        LocalDate date = LocalDate.now().minusYears(minAge);
        return !birthday.isAfter(date);
    }

    private boolean isYounger(int maxAge, LocalDate birthday) {
        LocalDate date = LocalDate.now().minusYears(maxAge + 1);
        return birthday.isAfter(date);
    }
}