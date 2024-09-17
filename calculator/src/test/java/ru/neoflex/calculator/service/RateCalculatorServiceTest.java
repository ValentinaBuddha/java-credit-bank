package ru.neoflex.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.config.RateConfiguration;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.neoflex.calculator.enums.EmploymentStatus.BUSINESS_OWNER;
import static ru.neoflex.calculator.enums.EmploymentStatus.EMPLOYED;
import static ru.neoflex.calculator.enums.EmploymentStatus.SELF_EMPLOYED;
import static ru.neoflex.calculator.enums.Gender.FEMALE;
import static ru.neoflex.calculator.enums.Gender.MALE;
import static ru.neoflex.calculator.enums.Gender.NON_BINARY;
import static ru.neoflex.calculator.enums.MaritalStatus.DIVORCED;
import static ru.neoflex.calculator.enums.MaritalStatus.MARRIED;
import static ru.neoflex.calculator.enums.MaritalStatus.SINGLE;
import static ru.neoflex.calculator.enums.MaritalStatus.WIDOW_WIDOWER;
import static ru.neoflex.calculator.enums.Position.MID_MANAGER;
import static ru.neoflex.calculator.enums.Position.TOP_MANAGER;
import static ru.neoflex.calculator.enums.Position.WORKER;

@ExtendWith(MockitoExtension.class)
class RateCalculatorServiceTest {

    @Mock
    private RateConfiguration rateConfiguration;

    @InjectMocks
    private RateCalculatorService rateCalculator;

    private Double creditRate = 19.00;
    private BigDecimal creditRateBigDec = BigDecimal.valueOf(creditRate);
    private EmploymentDto employment = EmploymentDto.builder()
            .employmentStatus(EMPLOYED)
            .position(WORKER)
            .build();
    private ScoringDataDto scoringData = ScoringDataDto.builder()
            .gender(FEMALE)
            .birthdate(LocalDate.now().minusYears(30))
            .maritalStatus(WIDOW_WIDOWER)
            .employment(employment)
            .build();

    @Test
    void calculateRate_whenNoInsuranceAndNotSalaryClient_thenRate19() {
        when(rateConfiguration.getRate()).thenReturn(creditRate);

        var rate = rateCalculator.calculateRate(false, false);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateRate_whenInsuranceAndNotSalaryClient_thenRate16() {
        when(rateConfiguration.getRate()).thenReturn(creditRate);

        var rate = rateCalculator.calculateRate(true, false);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateRate_whenNoInsuranceAndSalaryClient_thenRate18() {
        when(rateConfiguration.getRate()).thenReturn(creditRate);

        var rate = rateCalculator.calculateRate(false, true);

        assertEquals(BigDecimal.valueOf(18).setScale(2), rate);
    }

    @Test
    void calculateRate_whenInsuranceAndSalaryClient_thenRate18() {
        when(rateConfiguration.getRate()).thenReturn(creditRate);

        var rate = rateCalculator.calculateRate(true, true);

        assertEquals(BigDecimal.valueOf(15).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenEmployedStatus_thenRateNotChange() {
        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenSelfEmployedStatus_thenRateIncreasedBy1() {
        employment.setEmploymentStatus(SELF_EMPLOYED);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(20).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenBusinessOwnerStatus_thenRateIncreasedBy2() {
        employment.setEmploymentStatus(BUSINESS_OWNER);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(21).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenWorkerPosition_thenRateNotChange() {
        employment.setPosition(WORKER);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenPositionMiddleManager_thenRateDecreasedBy2() {
        employment.setPosition(MID_MANAGER);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(17).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenPositionTopManager_thenRateDecreasedBy3() {
        employment.setPosition(TOP_MANAGER);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenMarriedStatus_thenRateDecreasedBy3() {
        scoringData.setMaritalStatus(MARRIED);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenSingleStatus_thenRateNotChange() {
        scoringData.setMaritalStatus(SINGLE);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenDivorcedStatus_thenRateIncreasedBy1() {
        scoringData.setMaritalStatus(DIVORCED);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(20).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenWidowedStatus_thenRateNotChange() {
        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderNonBinary_thenRateIncreasedBy7() {
        scoringData.setGender(NON_BINARY);

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(26).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderFemaleAndAge32_thenRateDecreasedBy3() {
        scoringData.setBirthdate(LocalDate.now().minusYears(32));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderFemaleAndAge32PlusDay_thenRateDecreasedBy3() {
        scoringData.setBirthdate(LocalDate.now().minusYears(32).minusDays(1));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderFemaleAndAge61_thenRateNotChange() {
        scoringData.setBirthdate(LocalDate.now().minusYears(61));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderMaleAndAge30_thenRateNotChange() {
        scoringData.setGender(MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(30).plusDays(1));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderMaleAndAge30_thenRateDecreasedBy3() {
        scoringData.setGender(MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(30));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderMaleAndAge30PlusDay_thenRateDecreasedBy3() {
        scoringData.setGender(MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(30).minusDays(1));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(16).setScale(2), rate);
    }

    @Test
    void calculateFinalRate_whenGenderMaleAndAge56_thenRateNotChange() {
        scoringData.setGender(MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(56));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(19).setScale(2), rate);
    }

    @ParameterizedTest
    @CsvSource({
            "31, 19",
            "59, 16",
            "60, 16"
    })
    void calculateFinalRate_forFemaleWithDifferentAges(int age, int expectedRate) {
        scoringData.setBirthdate(LocalDate.now().minusYears(age + 1).plusDays(1));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(expectedRate).setScale(2), rate);
    }

    @ParameterizedTest
    @CsvSource({
            "54, 16",
            "55, 16"
    })
    void calculateFinalRate_forMaleWithDifferentAges_thenRateDecreasedBy3(int age, int expectedRate) {
        scoringData.setBirthdate(LocalDate.now().minusYears(age + 1).plusDays(1));

        var rate = rateCalculator.calculateFinalRate(scoringData, creditRateBigDec);

        assertEquals(BigDecimal.valueOf(expectedRate).setScale(2), rate);
    }
}
