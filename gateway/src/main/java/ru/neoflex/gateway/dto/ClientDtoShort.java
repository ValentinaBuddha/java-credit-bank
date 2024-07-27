package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Short client data for statement list.
 *
 * @author Valentina Vakhlamova
 */
@Schema(description = "Сокращённые данные клиента")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDtoShort {

    @Schema(description = "Уникальный идентификатор клиента", example = "3422b448-2460-4fd2-9183-8000de6f8343")
    private UUID id;

    @Schema(description = "Имя", example = "Ivan")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;
}
