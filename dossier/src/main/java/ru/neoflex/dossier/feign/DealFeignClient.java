package ru.neoflex.dossier.feign;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.dossier.config.DealFeignClientConfiguration;
import ru.neoflex.dossier.dto.StatementDto;
import ru.neoflex.dossier.enums.Status;


@FeignClient(value = "deal", url = "${deal.url}", configuration = DealFeignClientConfiguration.class)
public interface DealFeignClient {

    @PostMapping(value = "/admin/statement/{statementId}/status")
    void saveNewStatementStatus(@PathVariable String statementId, @RequestParam Status status);

    @PostMapping("/admin/statement/{statementId}")
    StatementDto findStatementById(@PathVariable @Parameter(required = true) String statementId);
}
