package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.Status;

import java.time.LocalDateTime;

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
    private LocalDateTime time;

    @Schema(description = "Каким образом был изменен статус заявки", example = "MANUAL")
    private ChangeType changeType;
}
