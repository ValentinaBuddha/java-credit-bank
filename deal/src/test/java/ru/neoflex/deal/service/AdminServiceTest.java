package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.StatementDtoFull;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.reposiory.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;
import static ru.neoflex.deal.enums.ChangeType.MANUAL;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private StatementRepository statementRepository;
    @Mock
    private StatementMapper statementMapper;
    @InjectMocks
    private AdminService adminService;

    private UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private Statement statement = Statement.builder()
            .id(id)
            .statusHistory(new ArrayList<>())
            .build();

    @Test
    void saveStatementStatusWithStatement() {
        assertDoesNotThrow(() -> adminService.saveStatementStatus(statement, PREAPPROVAL, AUTOMATIC));

        assertEquals(PREAPPROVAL, statement.getStatus());
        assertEquals(1, statement.getStatusHistory().size());
        assertEquals(PREAPPROVAL, statement.getStatusHistory().get(0).getStatus());
        assertEquals(AUTOMATIC, statement.getStatusHistory().get(0).getChangeType());
    }

    @Test
    void saveStatementStatusWithStatementId_whenStatementFound_thenStatusSaved() {
        when(statementRepository.findById(any())).thenReturn(Optional.ofNullable(statement));

        assertDoesNotThrow(() -> adminService.saveStatementStatus(String.valueOf(id), PREAPPROVAL, MANUAL));

        assertEquals(PREAPPROVAL, Objects.requireNonNull(statement).getStatus());
        assertEquals(1, statement.getStatusHistory().size());
        assertEquals(PREAPPROVAL, statement.getStatusHistory().get(0).getStatus());
        assertEquals(MANUAL, statement.getStatusHistory().get(0).getChangeType());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void saveStatementStatusWithStatementId_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        Executable executable = () -> adminService.saveStatementStatus(String.valueOf(id), PREAPPROVAL, MANUAL);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, executable);

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void findStatementById_whenStatementFound_thenReturnStatementDto() {
        when(statementRepository.findById(any())).thenReturn(Optional.ofNullable(statement));
        when(statementMapper.toStatementDtoFull(any())).thenReturn(new StatementDtoFull());

        assertDoesNotThrow(() -> adminService.findStatementById(String.valueOf(id)));

        verify(statementRepository, times(1)).findById(any());
        verify(statementMapper, times(1)).toStatementDtoFull(any());
    }

    @Test
    void findStatementById_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        Executable executable = () -> adminService.findStatementById(String.valueOf(id));
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, executable);

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }
}
