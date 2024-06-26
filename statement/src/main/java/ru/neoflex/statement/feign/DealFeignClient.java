package ru.neoflex.statement.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.statement.config.CustomFeignClientConfiguration;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(value = "deal", url = "${deal.url}", configuration = CustomFeignClientConfiguration.class)
public interface DealFeignClient {

    @PostMapping(value = "/statement")
    List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto loanStatement);

    @PostMapping(value = "/offer/select")
    void selectLoanOffers(@RequestBody LoanOfferDto loanOffer);
}
