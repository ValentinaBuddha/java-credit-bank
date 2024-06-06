package ru.neoflex.calculator.service;

import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;

import java.util.List;

/**
 * Describes the functionality of the service for creating loan offers.
 *
 * @author Valentina Vakhlamova
 */
public interface LoanOfferService {

    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement);
}
