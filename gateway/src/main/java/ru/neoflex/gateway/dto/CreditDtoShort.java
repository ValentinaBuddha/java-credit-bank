package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Short credit data for statement list.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Сокращённые данные по кредиту")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDtoShort {

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

    @Schema(description = "Статус кредита", example = "CALCULATED")
    private CreditStatus creditStatus;
}
