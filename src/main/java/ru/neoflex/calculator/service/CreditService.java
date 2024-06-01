package ru.neoflex.calculator.service;

import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.ScoringDataDto;

/**
 * Describes the functionality of the service for calculation of credit.
 *
 * @author Valentina Vakhlamova
 */
public interface CreditService {

    CreditDto calculateCredit(ScoringDataDto scoringData);
}
