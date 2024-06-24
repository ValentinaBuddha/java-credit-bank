package ru.neoflex.calculator.service;

import ru.neoflex.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;

/**
 * Describes the functionality of the service for rate calculation.
 *
 * @author Valentina Vakhlamova
 */
public interface RateCalculatorService {

    BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculateFinalRate(ScoringDataDto scoringData, BigDecimal previousRate);
}
