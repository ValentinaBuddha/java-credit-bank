package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.model.jsonb.AppliedOffer;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    AppliedOffer toAppliedOffer(LoanOfferDto loanOffer);

    LoanOfferDto toLoanOfferDto(AppliedOffer appliedOffer);
}
