package ru.neoflex.dossier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.dossier.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Заявка на кредит")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDto {
    private UUID id;
    private CreditDto credit;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime signDate;
    private String sesCode;
}