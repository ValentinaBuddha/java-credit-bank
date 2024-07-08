package ru.neoflex.deal.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.deal.config.CalculatorFeignClientConfiguration;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;

import java.util.List;

@FeignClient(value = "calculator", url = "${calculator.url}", configuration = CalculatorFeignClientConfiguration.class)
public interface CalculatorFeignClient {

    @PostMapping(value = "/offers")
    List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto loanStatement);

    @PostMapping(value = "/calc")
    CreditDto calculateCredit(@RequestBody ScoringDataDto scoringData);
}
