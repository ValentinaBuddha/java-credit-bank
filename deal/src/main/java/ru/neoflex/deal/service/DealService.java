package ru.neoflex.deal.service;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;

import java.util.List;

/**
 * Describes the functionality of the service for credit parameters calculation and saving data.
 *
 * @author Valentina Vakhlamova
 */
public interface DealService {

    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement);

    void selectLoanOffers(LoanOfferDto loanOffer);

    void finishRegistration(String statementId, FinishRegistrationRequestDto finishRegistration);
}