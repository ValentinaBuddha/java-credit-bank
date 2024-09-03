package ru.neoflex.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.gateway.config.FeignClientConfiguration;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(value = "statement", url = "${statement.url}", configuration = FeignClientConfiguration.class)
public interface StatementFeignClient {

    @PostMapping()
    List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto loanStatement);

    @PostMapping("/offer")
    void selectLoanOffers(@RequestBody LoanOfferDto loanOffer);
}
