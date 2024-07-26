package ru.neoflex.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.gateway.config.FeignClientConfiguration;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.StatementDtoFull;
import ru.neoflex.gateway.dto.StatementDtoShort;
import ru.neoflex.gateway.enums.Status;

import java.util.List;

@FeignClient(value = "deal", url = "${deal.url}", configuration = FeignClientConfiguration.class)
public interface DealFeignClient {

    @PostMapping("/calculate/{statementId}")
    void finishRegistration(@PathVariable String statementId,
                            @RequestBody FinishRegistrationRequestDto finishRegistration);

    @PostMapping("/document/{statementId}/send")
    void sendDocuments(@PathVariable String statementId);

    @PostMapping("/document/{statementId}/sign")
    void signDocuments(@PathVariable String statementId);

    @PostMapping("/document/{statementId}/code")
    void verifySesCode(@PathVariable String statementId, @RequestBody String code);

    @PutMapping(value = "/admin/statement/{statementId}/status")
    void saveStatementStatus(@PathVariable String statementId, @RequestParam Status status);

    @GetMapping("/admin/statement")
    List<StatementDtoShort> findAllStatements();

    @GetMapping("/admin/statement/{statementId}")
    StatementDtoFull findStatementById(@PathVariable String statementId);
}
