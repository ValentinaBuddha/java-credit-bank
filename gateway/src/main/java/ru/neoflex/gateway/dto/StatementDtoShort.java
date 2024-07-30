package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Short statement data.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Сокращённые данные по заявке")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDtoShort {

    @Schema(description = "Уникальный идентификатор заявки", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    private ClientDtoShort client;

    private CreditDtoShort credit;

    @Schema(description = "Статус заявки", example = "PREAPPROVAL")
    private Status status;

    @Schema(description = "Дата создания заявки", example = "2024-01-01")
    private LocalDateTime creationDate;

    @Schema(description = "Дата подписания кредитных документов", example = "2024-01-01")
    private LocalDateTime signDate;
}
