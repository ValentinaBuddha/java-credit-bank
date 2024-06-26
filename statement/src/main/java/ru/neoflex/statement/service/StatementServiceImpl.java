package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.feign.DealFeignClient;

import java.util.List;

/**
 * Service for calculation of loan offers and choosing one of them.
 *
 * @author Valentina Vakhlamova
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {

    private final DealFeignClient dealFeignClient;

    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Calculate loan offers in statementService: loanStatement = {}", loanStatement);
        return dealFeignClient.calculateLoanOffers(loanStatement);
    }

    @Override
    public void selectLoanOffers(LoanOfferDto loanOffer) {
        log.info("Select one loan offer in statementService = {}", loanOffer);
        dealFeignClient.selectLoanOffers(loanOffer);
    }
}
