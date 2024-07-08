package ru.neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.deal.util.DateConstant.DATE_PATTERN;

/**
 * Information about monthly payment for payment schedule.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "График ежемесячных платежей по кредиту")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа", example = "10")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2024-01-11")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate date;

    @Schema(description = "Ежемесячный платеж", example = "3060.55")
    private BigDecimal totalPayment;

    @Schema(description = "Сумма процентов", example = "100.53")
    private BigDecimal interestPayment;

    @Schema(description = "Основной долг", example = "2960.02")
    private BigDecimal debtPayment;

    @Schema(description = "Остаток долга", example = "250000.25")
    private BigDecimal remainingDebt;
}
