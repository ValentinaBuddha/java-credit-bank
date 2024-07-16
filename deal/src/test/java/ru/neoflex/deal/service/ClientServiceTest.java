package ru.neoflex.deal.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.exception.EmailExistsException;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.EmploymentDataMapper;
import ru.neoflex.deal.mapper.PassportDataMapper;
import ru.neoflex.deal.mapper.PassportMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.jsonb.EmploymentData;
import ru.neoflex.deal.model.jsonb.PassportData;
import ru.neoflex.deal.reposiory.ClientRepository;
import ru.neoflex.deal.reposiory.EmploymentRepository;
import ru.neoflex.deal.reposiory.PassportRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PassportRepository passportRepository;
    @Mock
    private EmploymentRepository employmentRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private PassportMapper passportMapper;
    @Mock
    private PassportDataMapper passportDataMapper;
    @Mock
    private EmploymentDataMapper employmentDataMapper;
    @InjectMocks
    private ClientService clientService;

    private final String email = "ivan@gmail.com";
    private final LoanStatementRequestDto loanStatement = LoanStatementRequestDto.builder()
            .email(email)
            .build();
    private final PassportData passportData = new PassportData();
    private final Passport passport = new Passport(passportData);
    private final Client client = Client.builder()
            .email(email)
            .passport(passport)
            .build();
    private final EmploymentDto employmentDto = new EmploymentDto();
    private final EmploymentData employmentData = new EmploymentData();
    private final Employment employment = new Employment(employmentData);
    private final FinishRegistrationRequestDto finishRegistration = FinishRegistrationRequestDto.builder()
            .employment(employmentDto)
            .build();

    @Test
    void saveClient_whenClientDoesNotExistInDb_thenClientSaved() {
        when(clientRepository.existsByEmail(email)).thenReturn(false);
        when(passportDataMapper.toPassportData(any(), any())).thenReturn(passportData);
        when(passportMapper.toPassport(passportData)).thenReturn(passport);
        when(passportRepository.save(passport)).thenReturn(passport);
        client.setEmail(email);
        client.setPassport(passport);
        when(clientMapper.toClient(loanStatement, passport)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);

        Assertions.assertDoesNotThrow(() -> clientService.saveClient(loanStatement));

        verify(clientRepository, times(1)).existsByEmail(email);
        verify(passportDataMapper, times(1)).toPassportData(any(), any());
        verify(passportMapper, times(1)).toPassport(passportData);
        verify(passportRepository, times(1)).save(passport);
        verify(clientMapper, times(1)).toClient(loanStatement, passport);
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void saveClient_whenClientExists_thenClientGet() {
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        when(clientRepository.getClientByEmail(email)).thenReturn(client);

        Assertions.assertDoesNotThrow(() -> clientService.saveClient(loanStatement));

        verify(clientRepository, times(1)).existsByEmail(email);
        verify(clientRepository, times(1)).getClientByEmail(email);
    }

    @Test
    void saveClient_whenClientExistsAndDoesNotMatch_thenExceptionThrows() {
        when(clientRepository.existsByEmail(email)).thenReturn(true);
        client.setFirstName("Masha");
        when(clientRepository.getClientByEmail(email)).thenReturn(client);

        final EmailExistsException exception = assertThrows(EmailExistsException.class, () ->
                clientService.saveClient(loanStatement));

        verify(clientRepository, times(1)).existsByEmail(email);
        verify(clientRepository, times(1)).getClientByEmail(email);
        assertEquals("Client with email ivan@gmail.com already exists. Use other email.",
                exception.getMessage());
    }

    @Test
    void finishRegistration() {
        when(employmentDataMapper.toEmploymentData(employmentDto)).thenReturn(employmentData);
        when(employmentRepository.save(any())).thenReturn(employment);
        when(passportDataMapper.toFullPassportData(any(), any(), any())).thenReturn(passportData);
        when(passportMapper.toFullPassport(any(), any())).thenReturn(passport);
        when(clientMapper.toFullClient(any(), any(), any(), any())).thenReturn(client);

        assertDoesNotThrow(() -> clientService.finishRegistration(client, finishRegistration));

        verify(employmentDataMapper, times(1)).toEmploymentData(any());
        verify(employmentRepository, times(1)).save(any());
        verify(passportDataMapper, times(1)).toFullPassportData(any(), any(), any());
        verify(passportMapper, times(1)).toFullPassport(any(), any());
        verify(clientMapper, times(1))
                .toFullClient(any(), any(), any(), any());
    }
}