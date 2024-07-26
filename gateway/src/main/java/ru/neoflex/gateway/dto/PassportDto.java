package ru.neoflex.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import static ru.neoflex.gateway.util.DateConstant.DATE_PATTERN;

/**
 * Passport data.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Паспортные данные клиента")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportDto {

    @Schema(description = "Уникальный идентификатор паспорта", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "1990-01-01")
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение выдачи паспорта", example = "ОВД кировского района города Пензы")
    private String passportIssueBranch;
}
