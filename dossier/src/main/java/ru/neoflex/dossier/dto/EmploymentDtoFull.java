package ru.neoflex.dossier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.dossier.enums.EmploymentStatus;
import ru.neoflex.dossier.enums.Position;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Full job information.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Полные сведения о текущей работе")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDtoFull {

    @Schema(description = "Уникальный идентификатор данных о работе", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    @Schema(description = "Рабочий статус", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707123456")
    private String employerInn;

    @Schema(description = "Размер заработной платы", example = "70000")
    private BigDecimal salary;

    @Schema(description = "Позиция на работе", example = "TOP_MANAGER")
    private Position position;

    @Schema(description = "Общий стаж работы", example = "256")
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "12")
    private Integer workExperienceCurrent;
}
