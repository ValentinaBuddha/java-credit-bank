package ru.neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

import static ru.neoflex.deal.util.DateConstant.DATE_PATTERN;
import static ru.neoflex.deal.util.StringPatterns.ACCOUNT_NUMBER;

/**
 * Information for finish registration.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Данные для завершения регистрации")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishRegistrationRequestDto {

    @Schema(description = "Пол", example = "MALE")
    @NotNull(message = "Необходимо выбрать пол")
    private Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED")
    @NotNull(message = "Необходимо выбрать семейное положение")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "0")
    @NotNull(message = "Необходимо заполнить количество иждивенцев")
    @PositiveOrZero(message = "Необходимо указать 0 или больше")
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта", example = "1990-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    @NotNull(message = "Необходимо указать дату выдачи паспорта")
    @PastOrPresent(message = "Дата выдачи паспорта должна быть не позже текущей даты")
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение выдачи паспорта", example = "ОВД кировского района города Пензы")
    @NotBlank(message = "Необходимо указать отделение выдачи паспорта")
    private String passportIssueBranch;

    @Schema(description = "Сведения о текущей работе")
    @NotNull(message = "Необходимо указать сведения о текущей работе")
    @Valid
    private EmploymentDto employment;

    @Schema(description = "Номер расчетного счёта", example = "40817810100007408755")
    @NotBlank(message = "Необходимо ввести номер расчетного счёта")
    @Pattern(regexp = ACCOUNT_NUMBER, message = "Счёт должен содержать 20 цифр")
    private String accountNumber;
}