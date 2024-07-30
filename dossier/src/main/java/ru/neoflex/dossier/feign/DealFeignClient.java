package ru.neoflex.dossier.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.dossier.config.DealFeignClientConfiguration;
import ru.neoflex.dossier.dto.StatementDtoFull;
import ru.neoflex.dossier.enums.Status;

@FeignClient(value = "deal", url = "${deal.url}", configuration = DealFeignClientConfiguration.class)
public interface DealFeignClient {

    @PutMapping(value = "/admin/statement/{statementId}/status")
    void saveStatementStatus(@PathVariable String statementId, @RequestParam Status status);

    @GetMapping("/admin/statement/{statementId}")
    StatementDtoFull findStatementById(@PathVariable String statementId);
}
