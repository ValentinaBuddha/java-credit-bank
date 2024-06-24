package ru.neoflex.calculator.service;

import ru.neoflex.calculator.dto.ScoringDataDto;

/**
 * Describes the functionality of the service for full data validation for credit.
 *
 * @author Valentina Vakhlamova
 */
public interface ScoringService {

    void scoring(ScoringDataDto scoringData);
}