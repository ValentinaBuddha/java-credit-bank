package ru.neoflex.statement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.statement.util.DateConstant.DATE_PATTERN;

/**
 * Loan statement after short scoring.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Заявка на кредит")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatementRequestDto {

    @Schema(description = "Запрашиваемая сумма кредита", example = "100000")
    @NotNull(message = "Необходимо указать сумму кредита")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "6")
    @NotNull(message = "Необходимо указать срок кредита в месяцах")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    @NotBlank(message = "Необходимо указать имя")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @NotBlank(message = "Необходимо указать фамилию")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;

    @Schema(description = "Электронная почта", example = "ivan@gmail.com")
    @NotBlank(message = "Необходимо указать электронную почту")
    private String email;

    @Schema(description = "Дата рождения", example = "1986-11-15")
    @NotNull(message = "Необходимо указать дату рождения")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    @NotBlank(message = "Необходимо указать серию паспорта")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    @NotBlank(message = "Необходимо указать номер паспорта")
    private String passportNumber;
}
