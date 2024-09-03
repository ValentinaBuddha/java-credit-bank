package ru.neoflex.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.neoflex.gateway.util.DateConstant.DATE_TIME_PATTERN;

/**
 * Full statement data.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Полные данные по заявке")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDtoFull {

    @Schema(description = "Уникальный идентификатор заявки", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    private ClientDtoFull client;

    private CreditDtoFull credit;

    @Schema(description = "Статус заявки", example = "PREAPPROVAL")
    private Status status;

    @Schema(description = "Дата создания заявки", example = "2024-01-01T00:00:00")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime creationDate;

    private LoanOfferDto appliedOffer;

    @Schema(description = "Дата подписания кредитных документов", example = "2024-01-01T00:00:00")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime signDate;

    @Schema(description = "Электронный код подписи документов", example = "123456")
    private String sesCode;

    private List<StatementStatusHistoryDto> statusHistory;
}
