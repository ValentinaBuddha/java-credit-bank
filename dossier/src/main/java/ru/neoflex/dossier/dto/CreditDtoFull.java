package ru.neoflex.dossier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.dossier.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Full final credit parameters.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Полные финальные параметры по кредиту")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDtoFull {

    @Schema(description = "Уникальный идентификатор кредита", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    @Schema(description = "Одобренная сумма кредита (с учетом страховки)", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "3060.55")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка по кредиту", example = "17")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита", example = "20.15")
    private BigDecimal psk;

    @Schema(description = "Включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Наличие зарплатного клиента в банке", example = "true")
    private Boolean isSalaryClient;

    @Schema(description = "График платежей")
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Schema(description = "Статус кредита", example = "CALCULATED")
    private CreditStatus creditStatus;
}
