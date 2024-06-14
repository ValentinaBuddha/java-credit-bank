package ru.neoflex.deal.service;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {

    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement);

    void selectLoanOffers(LoanOfferDto loanOffer);

    void finishRegistration(String statementId, FinishRegistrationRequestDto finishRegistration);
}