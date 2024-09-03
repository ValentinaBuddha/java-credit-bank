package ru.neoflex.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Необходимо указать идентификатор заявки")
    private UUID statementId;

    @Schema(description = "Запрошенная сумма кредита", example = "100000")
    @NotNull(message = "Необходимо указать запрашивамую сумму кредита")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита (с учетом страховки)", example = "110000.55")
    @NotNull(message = "Необходимо указать общую сумму кредита")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    @NotNull(message = "Необходимо указать срок кредита")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "3060.00")
    @NotNull(message = "Необходимо указать ежемесячный платеж по кредиту")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка по кредиту", example = "7")
    @NotNull(message = "Необходимо указать ставку по кредиту")
    private BigDecimal rate;

    @Schema(description = "Включена ли страховка", example = "true")
    @NotNull(message = "Необходимо указать включена ли страховка")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "true")
    @NotNull(message = "Необходимо указать наличие зарплатного клиента в банке")
    private Boolean isSalaryClient;
}
