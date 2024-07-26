package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Schema(description = "Имя", example = "Ivan")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;
}
