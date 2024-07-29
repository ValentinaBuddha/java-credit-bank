package ru.neoflex.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.exception.StatementStatusException;
import ru.neoflex.gateway.feign.DealFeignClient;

import static ru.neoflex.gateway.enums.Status.CC_APPROVED;
import static ru.neoflex.gateway.enums.Status.CC_DENIED;
import static ru.neoflex.gateway.enums.Status.DOCUMENT_CREATED;

/**
 * API for sending requests to DealMS.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Gateway: документы", description = "Отправляет запросы в DealMS.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/document/{statementId}")
public class DocumentController {

    private final DealFeignClient dealFeignClient;

    /**
     * Create and send credit documents.
     */
    @Operation(summary = "Запрос на отправку документов.")
    @PostMapping()
    public void sendDocuments(@PathVariable @Parameter(required = true) String statementId) {
        var statement = dealFeignClient.findStatementById(statementId);
        if (statement.getStatus() == CC_APPROVED) {
            dealFeignClient.sendDocuments(statementId);
        } else if (statement.getStatus() == CC_DENIED) {
            throw new StatementStatusException("Statement was denied.");
        } else {
            throw new StatementStatusException("For choosing loan offer statement status should be CC_APPROVED.");
        }
    }

    /**
     * Send ses code for signing credit documents.
     */
    @Operation(summary = "Запрос на подписание документов.")
    @PostMapping("/sign")
    public void signDocuments(@PathVariable @Parameter(required = true) String statementId) {
        var statement = dealFeignClient.findStatementById(statementId);
        if (statement.getStatus() == DOCUMENT_CREATED) {
            dealFeignClient.signDocuments(statementId);
        } else if (statement.getStatus() == CC_DENIED) {
            throw new StatementStatusException("Statement was denied.");
        } else {
            throw new StatementStatusException("For choosing loan offer statement status should be DOCUMENT_CREATED.");
        }
    }

    /**
     * Verify ses code for signing documents and issue credit.
     */
    @Operation(summary = "Подписание документов.")
    @PostMapping("/sign/code")
    public void verifySesCode(@PathVariable @Parameter(required = true) String statementId,
                              @RequestBody @Parameter(required = true) String code) {
        var statement = dealFeignClient.findStatementById(statementId);
        if (statement.getStatus() == DOCUMENT_CREATED) {
            dealFeignClient.verifySesCode(statementId, code);
        } else if (statement.getStatus() == CC_DENIED) {
            throw new StatementStatusException("Statement was denied.");
        } else {
            throw new StatementStatusException("For choosing loan offer statement status should be DOCUMENT_CREATED.");
        }
    }
}