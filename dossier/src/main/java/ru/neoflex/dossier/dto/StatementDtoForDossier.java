package ru.neoflex.dossier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Credit data and sesCode from statement for Dossier.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Данные по кредиту и sesCode из заявки для Досье")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDtoForDossier {
    private UUID id;
    private CreditDto credit;
    private String sesCode;
}