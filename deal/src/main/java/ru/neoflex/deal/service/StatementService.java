package ru.neoflex.deal.service;

import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.model.Statement;

public interface StatementService {

    void saveStatementStatus(Statement statement, Status status);

    void saveStatementStatus(String statementId, Status status);

    StatementDto findStatementById(String statementId);
}
