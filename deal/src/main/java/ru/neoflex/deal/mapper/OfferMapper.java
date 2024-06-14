package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.model.jsonb.AppliedOffer;

public final class OfferMapper {

    public static AppliedOffer toEntity(LoanOfferDto loanOffer) {

        return new AppliedOffer(
                loanOffer.getStatementId(),
                loanOffer.getRequestedAmount(),
                loanOffer.getTotalAmount(),
                loanOffer.getTerm(),
                loanOffer.getMonthlyPayment(),
                loanOffer.getRate(),
                loanOffer.getIsInsuranceEnabled(),
                loanOffer.getIsSalaryClient());
    }

    private OfferMapper() {
    }
}
