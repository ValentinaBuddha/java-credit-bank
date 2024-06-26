package ru.neoflex.statement.service;

import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement);

    void selectLoanOffers(LoanOfferDto loanOffer);
}
