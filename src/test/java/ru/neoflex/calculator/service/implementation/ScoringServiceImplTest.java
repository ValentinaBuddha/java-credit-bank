package ru.neoflex.calculator.service.implementation;

import org.junit.jupiter.api.Test;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.neoflex.calculator.dto.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.calculator.dto.enums.EmploymentStatus.UNEMPLOYED;

class ScoringServiceImplTest {

    private final ScoringService scoringService = new ScoringServiceImpl();
    private final LocalDate birthdateSixtyFiveYearsAge = LocalDate.now().minusYears(65);
    private final LocalDate birthdateTwentyYearsAge = LocalDate.now().minusYears(20);
    private final EmploymentDto employment = EmploymentDto.builder()
            .employmentStatus(SELF_EMPLOYED)
            .salary(BigDecimal.valueOf(300000))
            .workExperienceTotal(19)
            .workExperienceCurrent(4)
            .build();
    private final ScoringDataDto scoringData = ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(100000))
            .birthdate(LocalDate.of(1986, 11, 16))
            .employment(employment)
            .build();

    @Test
    void testScoring_whenValidData_thenNoExceptionThrown() {
        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenUnemployedStatus_thenThrowsException() {
        employment.setEmploymentStatus(UNEMPLOYED);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. Working status unemployed.", exception.getMessage());
    }

    @Test
    void scoring_whenAmountExceeds25Salaries_thenThrowsException() {
        BigDecimal smallSalary = BigDecimal.valueOf(3000);
        employment.setSalary(smallSalary);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. The loan amount is more than 25 salaries.", exception.getMessage());
    }

    @Test
    void scoring_whenAgeMoreSixtyFiveYearsAgo_thenThrowsException() {
        LocalDate birthdateSixtyFiveYearsAgoYearsAgeMinusDay = birthdateSixtyFiveYearsAge.minusDays(1);
        scoringData.setBirthdate(birthdateSixtyFiveYearsAgoYearsAgeMinusDay);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. Age over 65 years.", exception.getMessage());
    }

    @Test
    void scoring_whenAgeSixtyFive_thenNoExceptionThrown() {
        scoringData.setBirthdate(birthdateSixtyFiveYearsAge);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeLessSixtyFive_thenNoExceptionThrown() {
        LocalDate birthdateSixtyFiveYearsAgoYearsAgePlusDay = birthdateSixtyFiveYearsAge.plusDays(1);
        scoringData.setBirthdate(birthdateSixtyFiveYearsAgoYearsAgePlusDay);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeLessTwenty_thenThrowsException() {
        LocalDate birthdateTwentyYearsAgoYearsAgePlusDay = birthdateTwentyYearsAge.plusDays(1);
        scoringData.setBirthdate(birthdateTwentyYearsAgoYearsAgePlusDay);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. Age less than 20 years.", exception.getMessage());
    }

    @Test
    void scoring_whenAgeTwenty_thenNoExceptionThrown() {
        scoringData.setBirthdate(birthdateTwentyYearsAge);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenAgeMoreTwenty_thenNoExceptionThrown() {
        LocalDate birthdateTwentyYearsAgoMinusDay = birthdateTwentyYearsAge.minusDays(1);
        scoringData.setBirthdate(birthdateTwentyYearsAgoMinusDay);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }


    @Test
    void scoring_whenWorkExperienceTotalLessEighteen_thenThrowsException() {
        employment.setWorkExperienceTotal(17);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. Total experience less than 18 months.", exception.getMessage());
    }

    @Test
    void scoring_whenWorkExperienceTotalIsEighteen_thenNoExceptionThrown() {
        employment.setWorkExperienceTotal(18);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }

    @Test
    void scoring_whenWorkExperienceCurrentLessThree_thenThrowsException() {
        employment.setWorkExperienceCurrent(2);

        final ScoringException exception = assertThrows(ScoringException.class, () -> scoringService.scoring(scoringData));

        assertEquals("Rejection. Current experience less than 3 months.", exception.getMessage());
    }

    @Test
    void scoring_whenWorkExperienceCurrentIsThree_thenNoExceptionThrown() {
        employment.setWorkExperienceCurrent(3);

        assertDoesNotThrow(() -> scoringService.scoring(scoringData));
    }
}