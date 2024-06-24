package ru.neoflex.deal.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.exception.EmailExistsException;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.EmploymentDataMapper;
import ru.neoflex.deal.mapper.PassportDataMapper;
import ru.neoflex.deal.mapper.PassportMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.reposiory.ClientRepository;
import ru.neoflex.deal.reposiory.EmploymentRepository;
import ru.neoflex.deal.reposiory.PassportRepository;
import ru.neoflex.deal.service.ClientService;

import java.util.Objects;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PassportRepository passportRepository;
    private final EmploymentRepository employmentRepository;
    private final PassportDataMapper passportDataMapper;
    private final PassportMapper passportMapper;
    private final ClientMapper clientMapper;
    private final EmploymentDataMapper employmentDataMapper;

    @Override
    public Client saveClient(LoanStatementRequestDto loanStatement) {

        var email = loanStatement.getEmail();
        Client client;

        if (clientRepository.existsByEmail(email)) {
            log.info("Client with email {} already exists", email);

            client = clientRepository.getClientByEmail(email);
            log.info("Existing client = {}", client);

            if (checkClient(client, loanStatement)) {
                log.info("Full name, birthdate, series and number of passport match");
                return client;
            } else {
                throw new EmailExistsException(String.format("Client with email %s already exists. Use other email.", email));
            }

        } else {
            log.info("Client with email {} doesn't exist", email);

            var passportData = passportDataMapper.toPassportData(loanStatement.getPassportSeries(),
                    loanStatement.getPassportNumber());
            var passport = passportMapper.toPassport(passportData);
            var savedPassport = passportRepository.save(passport);
            log.info("Passport saved = {}", savedPassport);

            var newClient = clientMapper.toClient(loanStatement, savedPassport);
            client = clientRepository.save(newClient);
            log.info("Client saved = {}", client);
        }

        return client;
    }

    @Override
    public void finishRegistration(Client client, FinishRegistrationRequestDto finishRegistration) {
        var employmentData = employmentDataMapper.toEmploymentData(finishRegistration.getEmployment());
        var employment = employmentRepository.save(new Employment(employmentData));
        var fullPassportData = passportDataMapper.toFullPassportData(client.getPassport().getPassportData(),
                finishRegistration.getPassportIssueBranch(), finishRegistration.getPassportIssueDate());
        var fullPassport = passportMapper.toFullPassport(client.getPassport(), fullPassportData);
        var fullClient = clientMapper.toFullClient(client, finishRegistration, employment, fullPassport);
        log.info("Fields of client updated = {}", fullClient);
    }

    private boolean checkClient(Client client, LoanStatementRequestDto loanStatement) {
        return Objects.equals(client.getFirstName(), loanStatement.getFirstName()) &&
                Objects.equals(client.getLastName(), loanStatement.getLastName()) &&
                Objects.equals(client.getMiddleName(), loanStatement.getMiddleName()) &&
                Objects.equals(client.getBirthdate(), loanStatement.getBirthdate()) &&
                Objects.equals(client.getPassport().getPassportData().getSeries(), loanStatement.getPassportSeries()) &&
                Objects.equals(client.getPassport().getPassportData().getNumber(), loanStatement.getPassportNumber());
    }
}