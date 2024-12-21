package ru.neoflex.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.Gender;
import ru.neoflex.gateway.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

import static ru.neoflex.gateway.util.DateConstant.DATE_PATTERN;

/**
 * Full client data for statement list.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Полные данные клиента")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDtoFull {

    @Schema(description = "Уникальный идентификатор клиента", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastName;

    @Schema(description = "Имя", example = "Ivan")
    private String firstName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;

    @Schema(description = "Электронная почта", example = "ivan@gmail.com")
    private String email;

    @Schema(description = "Дата рождения", example = "1986-11-15")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthdate;

    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "0")
    private Integer dependentAmount;

    private PassportDto passport;

    private EmploymentDtoFull employment;

    @Schema(description = "Номер расчетного счёта", example = "40817810100007408755")
    private String account;
}
