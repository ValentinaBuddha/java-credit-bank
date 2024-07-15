package ru.neoflex.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.neoflex.deal.util.DateConstant.DATE_PATTERN;

@Schema(description = "Заявка на кредит")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDto {
    private UUID id;
    private CreditDto credit;
    private Status status;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime signDate;
    private String sesCode;
}