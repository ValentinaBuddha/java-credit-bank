package ru.neoflex.statement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.statement.annotation.MinEighteenYearsBeforeDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.neoflex.statement.util.DateConstant.DATE_PATTERN;
import static ru.neoflex.statement.util.StringPatterns.EMAIL;
import static ru.neoflex.statement.util.StringPatterns.LATIN_ALPHABET;
import static ru.neoflex.statement.util.StringPatterns.PASSPORT_NUMBER;
import static ru.neoflex.statement.util.StringPatterns.PASSPORT_SERIES;

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
    @Min(value = 30000, message = "Сумма кредита должна быть больше или равна 30 000.00 рублей")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "6")
    @NotNull(message = "Необходимо указать срок кредита в месяцах")
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6ти месяцам")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    @NotBlank(message = "Необходимо указать имя")
    @Pattern(regexp = LATIN_ALPHABET, message = "Имя должно содержать от 2 до 30 латинских букв")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @NotBlank(message = "Необходимо указать фамилию")
    @Pattern(regexp = LATIN_ALPHABET, message = "Фамилия должна содержать от 2 до 30 латинских букв")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    @Pattern(regexp = LATIN_ALPHABET, message = "Отчество должно содержать от 2 до 30 латинских букв")
    private String middleName;

    @Schema(description = "Электронная почта", example = "ivan@gmail.com")
    @NotBlank(message = "Необходимо указать электронную почту")
    @Pattern(regexp = EMAIL, message = "Поле email имеет некорректный формат")
    private String email;

    @Schema(description = "Дата рождения", example = "1986-11-15")
    @NotNull(message = "Необходимо указать дату рождения")
    @JsonFormat(pattern = DATE_PATTERN)
    @MinEighteenYearsBeforeDate(message = "Кредит выдаётся лицам, достигшим 18-ти лет")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    @NotBlank(message = "Необходимо указать серию паспорта")
    @Pattern(regexp = PASSPORT_SERIES, message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    @NotBlank(message = "Необходимо указать номер паспорта")
    @Pattern(regexp = PASSPORT_NUMBER, message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}
