package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Loan offer.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Кредитное предложение")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfferDto {

    @Schema(description = "Уникальный идентификатор заявки", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID statementId;

    @Schema(description = "Запрошенная сумма кредита", example = "100000")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита (с учетом страховки)", example = "100000")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита в месяцах", example = "6")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "17602.27")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка по кредиту", example = "19")
    private BigDecimal rate;

    @Schema(description = "Включена ли страховка", example = "false")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "false")
    private Boolean isSalaryClient;
}
