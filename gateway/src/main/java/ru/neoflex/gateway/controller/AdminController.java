package ru.neoflex.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.dto.StatementDtoFull;
import ru.neoflex.gateway.dto.StatementDtoShort;
import ru.neoflex.gateway.enums.Status;
import ru.neoflex.gateway.feign.DealFeignClient;

import java.util.List;

/**
 * API for sending requests for DealMS.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Gateway: админ", description = "Отправляет запросы в DealMS.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DealFeignClient dealFeignClient;

    /**
     * Change statement status.
     */
    @Operation(summary = "Сохранение статуса заявки.")
    @PutMapping("/{statementId}/status")
    public void saveStatementStatus(@PathVariable @Parameter(required = true) String statementId,
                                    @RequestParam @Parameter(required = true) Status status) {
        dealFeignClient.saveStatementStatus(statementId, status);
    }

    /**
     * Get statement by id.
     */
    @Operation(summary = "Получение заявки по идентификатору.")
    @GetMapping("/{statementId}")
    public StatementDtoFull findStatementById(@PathVariable @Parameter(required = true) String statementId) {
        return dealFeignClient.findStatementById(statementId);
    }

    /**
     * Get all statements.
     */
    @Operation(summary = "Получение всех заявок.")
    @GetMapping()
    public List<StatementDtoShort> findAllStatements() {
        return dealFeignClient.findAllStatements();
    }
}
