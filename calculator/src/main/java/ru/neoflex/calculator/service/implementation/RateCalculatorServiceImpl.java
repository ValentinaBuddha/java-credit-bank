package ru.neoflex.calculator.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.RateConfiguration;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.dto.enums.Gender;
import ru.neoflex.calculator.dto.enums.MaritalStatus;
import ru.neoflex.calculator.service.RateCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static ru.neoflex.calculator.dto.enums.EmploymentStatus.BUSINESS_OWNER;
import static ru.neoflex.calculator.dto.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.calculator.dto.enums.Gender.FEMALE;
import static ru.neoflex.calculator.dto.enums.Gender.MALE;
import static ru.neoflex.calculator.dto.enums.Gender.NON_BINARY;
import static ru.neoflex.calculator.dto.enums.MaritalStatus.DIVORCED;
import static ru.neoflex.calculator.dto.enums.MaritalStatus.MARRIED;
import static ru.neoflex.calculator.dto.enums.Position.MID_MANAGER;
import static ru.neoflex.calculator.dto.enums.Position.TOP_MANAGER;
import static ru.neoflex.calculator.util.BigDecimalConstant.SEVEN;
import static ru.neoflex.calculator.util.BigDecimalConstant.THREE;
import static ru.neoflex.calculator.util.BigDecimalConstant.TWO;

/**
 * Calculate credit parameters.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RateCalculatorServiceImpl implements RateCalculatorService {

    private final RateConfiguration rateConfiguration;

    @Override
    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("Calculating rate: isInsuranceEnabled = {}, isSalaryClient = {}", isInsuranceEnabled, isSalaryClient);

        var rate = BigDecimal.valueOf(rateConfiguration.getRate());

        if (Boolean.TRUE.equals(isInsuranceEnabled)) {
            rate = rate.subtract(THREE);
        }

        if (Boolean.TRUE.equals(isSalaryClient)) {
            rate = rate.subtract(BigDecimal.ONE);
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateFinalRate(ScoringDataDto scoringData, BigDecimal previousRate) {
        log.info("Final calculating rate: scoringData = {}, rate = {}", scoringData, previousRate);

        var rate = previousRate;
        EmploymentDto employment = scoringData.getEmployment();
        Gender gender = scoringData.getGender();
        LocalDate birthdate = scoringData.getBirthdate();
        MaritalStatus maritalStatus = scoringData.getMaritalStatus();

        if (employment.getEmploymentStatus() == SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.ONE);
        }

        if (employment.getEmploymentStatus() == BUSINESS_OWNER) {
            rate = rate.add(TWO);
        }

        if (employment.getPosition() == MID_MANAGER) {
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

        if (gender == FEMALE && isOlderAndYounger(32, 60, birthdate)) {
            rate = rate.subtract(THREE);

        }

        if (gender == MALE && isOlderAndYounger(30, 55, birthdate)) {
            rate = rate.subtract(THREE);
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isOlderAndYounger(int minAge, int maxAge, LocalDate birthdate) {
        LocalDate maxBirthdate = LocalDate.now().minusYears(minAge);
        LocalDate minBirthdate = LocalDate.now().minusYears(maxAge + 1L);
        return !birthdate.isAfter(maxBirthdate) && birthdate.isAfter(minBirthdate);
    }
}