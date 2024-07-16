package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.StatementDto;
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

import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final StatementRepository statementRepository;
    private final StatementMapper statementMapper;

    public void saveStatementStatus(Statement statement, Status status) {
        log.info("Save new statement status = {}", status);

        statement.setStatus(status);
        log.info("Status in statement saved = {}", statement.getStatus());

        var statementStatus = new StatementStatus(status, LocalDateTime.now(), AUTOMATIC);
        List<StatementStatus> history = statement.getStatusHistory();
        history.add(statementStatus);
        log.info("Status saved in history: {}",
                statement.getStatusHistory().stream().map(StatementStatus::toString).collect(Collectors.joining(", ")));
    }

    public void saveStatementStatus(String statementId, Status status) {
        var statement = findStatementById(UUID.fromString(statementId));
        log.info("Statement found = {}", statement);

        saveStatementStatus(statement, status);
    }

    public StatementDto findStatementById(String statementId) {
        var statement = findStatementById(UUID.fromString(statementId));
        log.info("Statement found = {}", statement);

        return statementMapper.toStatementDto(statement);
    }

    private Statement findStatementById(UUID id) {
        return statementRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", id)));
    }
}
