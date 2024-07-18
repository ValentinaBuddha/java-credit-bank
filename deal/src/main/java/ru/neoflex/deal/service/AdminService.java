package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.StatementDto;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.StatementStatus;
import ru.neoflex.deal.reposiory.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final StatementRepository statementRepository;
    private final StatementMapper statementMapper;

    public void saveStatementStatus(Statement statement, Status status, ChangeType changeType) {
        log.info("Save new statement status = {}", status);

        statement.setStatus(status);
        log.info("Status saved in statement");

        var statementStatus = new StatementStatus(status, LocalDateTime.now(), changeType);
        List<StatementStatus> history = statement.getStatusHistory();
        history.add(statementStatus);
        log.info("Status saved in history: {}", history.stream()
                .map(StatementStatus::toString)
                .collect(Collectors.joining(", ")));
    }

    public void saveStatementStatus(String statementId, Status status, ChangeType changeType) {
        var statement = findStatementById(UUID.fromString(statementId));
        saveStatementStatus(statement, status, changeType);
    }

    @Transactional(readOnly = true)
    public StatementDto findStatementById(String statementId) {
        var statement = findStatementById(UUID.fromString(statementId));
        return statementMapper.toStatementDto(statement);
    }

    private Statement findStatementById(UUID statementId) {
        var statement = statementRepository.findById(statementId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", statementId)));
        log.info("Statement found = {}", statement);
        return statement;
    }
}
