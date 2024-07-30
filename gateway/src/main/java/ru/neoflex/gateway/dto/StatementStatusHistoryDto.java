package ru.neoflex.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.ChangeType;
import ru.neoflex.gateway.enums.Status;

import java.time.LocalDateTime;

import static ru.neoflex.gateway.util.DateConstant.DATE_TIME_PATTERN;

/**
 * Status history element.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Элемент списка истории статусов")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatusHistoryDto {

    @Schema(description = "Статус заявки", example = "APPROVED")
    private Status status;

    @Schema(description = "Дата присвоения нового статуса", example = "2024-01-01T00:00:00")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime time;

    @Schema(description = "Каким образом был изменен статус заявки", example = "MANUAL")
    private ChangeType changeType;
}
