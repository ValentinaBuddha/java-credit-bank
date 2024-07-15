package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.service.DealService;
import ru.neoflex.deal.service.DocumentService;
import ru.neoflex.deal.service.StatementService;

import javax.validation.Valid;
import java.util.List;

/**
 * API for credit parameters calculation and saving data.
 *
 * @author Valentina Vakhlamova
 */
@Tag(name = "Сделка", description = "API по расчёту кредитных предложений и выбору одного из них, " +
        "по завершению регистрации с полным расчётом всех параметров по кредиту и сохранению данных в базу данных.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deal")
public class DealController {

    private final DealService dealService;
    private final DocumentService documentService;
    private final StatementService statementService;

    /**
     * Calculate 4 loan offers.
     */
    @Operation(summary = "Расчёт возможных условий кредита.")
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateLoanOffers(@Parameter(required = true) @Valid @RequestBody
                                                  LoanStatementRequestDto loanStatement) {
        return dealService.calculateLoanOffers(loanStatement);
    }

    /**
     * Select one loan offer.
     */
    @Operation(summary = "Выбор одного из предложений по кредиту.")
    @PostMapping("/offer/select")
    public void selectLoanOffers(@Parameter(required = true) @Valid @RequestBody LoanOfferDto loanOffer) {
        dealService.selectLoanOffers(loanOffer);
    }

    /**
     * Final registration and full calculation of credit parameters.
     */
    @Operation(summary = "Завершение регистрации и полный расчёт всех параметров по кредиту.")
    @PostMapping("/calculate/{statementId}")
    public void calculateCredit(@PathVariable @Parameter(required = true) String statementId,
                                @Parameter(required = true) @Valid @RequestBody
                                FinishRegistrationRequestDto finishRegistration) {
        dealService.finishRegistration(statementId, finishRegistration);
    }

    @Operation(summary = "Запрос на отправку документов.")
    @PostMapping("/document/{statementId}/send")
    public void sendDocuments(@PathVariable @Parameter(required = true) String statementId) {
        documentService.sendDocuments(statementId);
    }

    @Operation(summary = "Запрос на подписание документов.")
    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@PathVariable @Parameter(required = true) String statementId) {
        documentService.signDocuments(statementId);
    }

    @Operation(summary = "Подписание документов.")
    @PostMapping("/document/{statementId}/code")
    public void verifySesCode(@PathVariable @Parameter(required = true) String statementId,
                              @RequestBody String code) {
        documentService.verifySesCode(statementId, code);
    }

    @Operation(summary = "Сохранение нового статуса заявки.")
    @PutMapping("/admin/statement/{statementId}/status")
    public void findStatementById(@PathVariable @Parameter(required = true) String statementId,
                                  @RequestParam @Parameter(required = true) Status status) {
        statementService.saveStatementStatus(statementId, status);
    }

    @Operation(summary = "Получение заявки по идентификатору.")
    @PostMapping("/admin/statement/{statementId}")
    public StatementDto findStatementById(@PathVariable @Parameter(required = true) String statementId) {
        return statementService.findStatementById(statementId);
    }
}