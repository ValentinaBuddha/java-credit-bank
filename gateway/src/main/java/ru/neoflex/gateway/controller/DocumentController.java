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
import ru.neoflex.gateway.feign.DealFeignClient;

/**
 * API for sending requests for DealMS.
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
        dealFeignClient.sendDocuments(statementId);
    }

    /**
     * Send ses code for signing credit documents.
     */
    @Operation(summary = "Запрос на подписание документов.")
    @PostMapping("/sign")
    public void signDocuments(@PathVariable @Parameter(required = true) String statementId) {
        dealFeignClient.signDocuments(statementId);
    }

    /**
     * Verify ses code for signing documents and issue credit.
     */
    @Operation(summary = "Подписание документов.")
    @PostMapping("/sign/code")
    public void verifySesCode(@PathVariable @Parameter(required = true) String statementId,
                              @RequestBody @Parameter(required = true) String code) {
        dealFeignClient.verifySesCode(statementId, code);
    }
}