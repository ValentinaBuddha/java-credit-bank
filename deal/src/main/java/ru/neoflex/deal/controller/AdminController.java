package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.service.AdminService;

import static ru.neoflex.deal.enums.ChangeType.MANUAL;

@Tag(name = "Сделка: админ", description = "API по сохранению нового статуса заявки и получению заявки по идентификтору.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deal/admin/statement/{statementId}")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Сохранение нового статуса заявки.")
    @PutMapping("/status")
    public void findStatementById(@PathVariable @Parameter(required = true) String statementId,
                                  @RequestParam @Parameter(required = true) Status status) {
        adminService.saveStatementStatus(statementId, status, MANUAL);
    }

    @Operation(summary = "Получение заявки по идентификатору.")
    @PostMapping
    public StatementDto findStatementById(@PathVariable @Parameter(required = true) String statementId) {
        return adminService.findStatementById(statementId);
    }
}
