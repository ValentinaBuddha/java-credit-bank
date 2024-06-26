package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Position;

import java.math.BigDecimal;

/**
 * Job information.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Сведения о текущей работе")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDto {

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707123456")
    private String employerINN;

    @Schema(description = "Размер заработной платы", example = "70000")
    private BigDecimal salary;

    @Schema(description = "Позиция на работе", example = "TOP_MANAGER")
    private Position position;

    @Schema(description = "Общий стаж работы", example = "256")
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "12")
    private Integer workExperienceCurrent;
}