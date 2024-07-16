package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Заявка на кредит")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDto {
    private UUID id;
    private CreditDto credit;
    private String sesCode;
}