package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.exception.VerifySesCodeException;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.reposiory.StatementRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.neoflex.deal.enums.CreditStatus.ISSUED;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private StatementRepository statementRepository;
    @Mock
    private KafkaMessagingService kafkaMessagingService;
    @Mock
    private AdminService adminService;
    @InjectMocks
    private DocumentService documentService;

    private String id = "6dd2ff79-5597-4c58-9a88-55ab84c9378d";
    private Client client = Client.builder().email("ivan@gmail.com").build();
    private Statement statement = Statement.builder()
            .id(UUID.fromString(id))
            .client(client)
            .build();

    @Test
    void sendDocuments_whenStatementFound_thenDocsSent() {
        when(statementRepository.findById(any())).thenReturn(Optional.ofNullable(statement));

        assertDoesNotThrow(() -> documentService.sendDocuments(id));

        verify(statementRepository, times(1)).findById(any());
        verify(adminService, times(1)).saveStatementStatus((Statement) any(), any(), any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
    }

    @Test
    void sendDocuments_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                documentService.sendDocuments(id));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void signDocuments_whenStatementFound_thenCodeSent() {
        when(statementRepository.findById(any())).thenReturn(Optional.ofNullable(statement));

        assertDoesNotThrow(() -> documentService.signDocuments(id));

        verify(statementRepository, times(1)).findById(any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
        assertNotNull(Objects.requireNonNull(statement).getSesCode());
    }

    @Test
    void signDocuments_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                documentService.signDocuments(id));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void verifySesCode_whenSesCodeEquals_thenDocsSigned() {
        statement.setSesCode("123456");
        statement.setCredit(new Credit());
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));

        assertDoesNotThrow(() -> documentService.verifySesCode(id, "123456"));

        verify(statementRepository, times(1)).findById(any());
        verify(kafkaMessagingService, times(1)).sendMessage(any(), any());
        verify(adminService, times(2)).saveStatementStatus((Statement) any(), any(), any());
        assertNotNull(statement.getSignDate());
        assertEquals(ISSUED, statement.getCredit().getCreditStatus());
    }

    @Test
    void verifySesCode_whenSesCodeNotEquals_thenThrowsException() {
        statement.setSesCode("111111");
        when(statementRepository.findById(any())).thenReturn(Optional.of(statement));

        VerifySesCodeException exception = assertThrows(VerifySesCodeException.class, () ->
                documentService.verifySesCode(id, "123456"));

        assertEquals("Ses code is invalid.", exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }

    @Test
    void verifySesCode_whenStatementNotFound_thenThrowsException() {
        when(statementRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                documentService.verifySesCode(id, "123456"));

        assertEquals("Statement with id 6dd2ff79-5597-4c58-9a88-55ab84c9378d wasn't found",
                exception.getMessage());
        verify(statementRepository, times(1)).findById(any());
    }
}
