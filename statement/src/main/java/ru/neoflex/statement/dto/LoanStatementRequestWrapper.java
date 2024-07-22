package ru.neoflex.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.statement.enums.Status;

@Schema(description = "Заявка на кредит и статус заявки")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatementRequestWrapper {

    @Schema(description = "Заявка на кредит")
    private LoanStatementRequestDto loanStatement;

    @Schema(description = "Статус заявки")
    private Status status;
}
