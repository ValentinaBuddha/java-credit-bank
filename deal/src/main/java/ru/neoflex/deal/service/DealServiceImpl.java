package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.enums.Status;
import ru.neoflex.deal.exception.EmailExistsException;
import ru.neoflex.deal.exception.EntityNotFoundException;
import ru.neoflex.deal.feign.CalculatorFeignClient;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.EmploymentDataMapper;
import ru.neoflex.deal.mapper.OfferMapper;
import ru.neoflex.deal.mapper.PassportDataMapper;
import ru.neoflex.deal.mapper.PassportMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.StatementStatus;
import ru.neoflex.deal.reposiory.ClientRepository;
import ru.neoflex.deal.reposiory.CreditRepository;
import ru.neoflex.deal.reposiory.EmploymentRepository;
import ru.neoflex.deal.reposiory.PassportRepository;
import ru.neoflex.deal.reposiory.StatementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.neoflex.deal.enums.ChangeType.AUTOMATIC;
import static ru.neoflex.deal.enums.Status.CC_DENIED;
import static ru.neoflex.deal.enums.Status.CLIENT_DENIED;
import static ru.neoflex.deal.enums.Status.PREAPPROVAL;

/**
 * Service for credit parameters calculation and saving data.
 *
 * @author Valentina Vakhlamova
 */
@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class DealServiceImpl implements DealService {

    private final CalculatorFeignClient calculatorFeignClient;
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final PassportRepository passportRepository;
    private final EmploymentRepository employmentRepository;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final PassportDataMapper passportDataMapper;
    private final PassportMapper passportMapper;
    private final ClientMapper clientMapper;
    private final EmploymentDataMapper employmentDataMapper;
    private final ScoringDataMapper scoringDataMapper;

    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Calculate loan offers in dealService: loanStatement = {}", loanStatement);

        var client = saveClient(loanStatement);

        var statement = new Statement(client, LocalDateTime.now(), new ArrayList<>());
        var savedStatement = statementRepository.save(statement);
        log.info("Statement saved = {}", savedStatement);

        saveStatus(savedStatement, PREAPPROVAL);

        List<LoanOfferDto> offers = calculatorFeignClient.getLoanOffers(loanStatement);
        offers.forEach(offer -> offer.setStatementId(savedStatement.getId()));
        log.info("Offers get from CalculatorMS: {}",
                offers.stream().map(LoanOfferDto::toString).collect(Collectors.joining(", ")));

        return offers;
    }

    @Override
    public void selectLoanOffers(LoanOfferDto loanOffer) {
        log.info("Select one loan offer = {}", loanOffer);

        var statement = findStatementById(loanOffer.getStatementId());
        log.info("Statement has found = {}", statement);

        saveStatus(statement, CLIENT_DENIED);

        var appliedOffer = offerMapper.toAppliedOffer(loanOffer);
        statement.setAppliedOffer(appliedOffer);
        log.info("Statement with selected offer saved = {}", findStatementById(statement.getId()));
    }

    @Override
    public void finishRegistration(String statementId, FinishRegistrationRequestDto finishRegistration) {
        log.info("Finish registration: statementId = {}, finishRegistration = {}", statementId, finishRegistration);

        var statement = findStatementById(UUID.fromString(statementId));
        log.info("Statement found = {}", statement);

        var offer = statement.getAppliedOffer();
        var client = statement.getClient();
        var passportData = client.getPassport().getPassportData();
        var scoringData = scoringDataMapper.toScoringDto(statement, finishRegistration, offer, client, passportData);
        log.info("ScoringData prepared = {}", scoringData);

        var creditDto = calculatorFeignClient.calculateCredit(scoringData);
        log.info("CreditDto get from CalculatorMS = {}", creditDto);

        var credit = creditMapper.toCredit(creditDto);
        var savedCredit = creditRepository.save(credit);
        log.info("Credit saved = {}", savedCredit);

        saveStatus(statement, CC_DENIED);

        var employmentData = employmentDataMapper.toEmploymentData(scoringData.getEmployment());
        var employment = employmentRepository.save(new Employment(employmentData));
        var fullPassportData = passportDataMapper.toFullPassportData(client.getPassport().getPassportData(),
                finishRegistration.getPassportIssueBranch(), finishRegistration.getPassportIssueDate());
        var fullPassport = passportMapper.toFullPassport(client.getPassport(), fullPassportData);
        var fullClient = clientMapper.toFullClient(client, finishRegistration, employment, fullPassport);
        log.info("Fields of client updated = {}", fullClient);
    }

    private void saveStatus(Statement statement, Status status) {
        log.info("Save new statement status = {}", status);

        statement.setStatus(status);
        log.info("Status in statement saved = {}", statement.getStatus());

        var statementStatus = new StatementStatus(status, LocalDateTime.now(), AUTOMATIC);
        List<StatementStatus> history = statement.getStatusHistory();
        history.add(statementStatus);
        log.info("Status saved in history: {}",
                statement.getStatusHistory().stream().map(StatementStatus::toString).collect(Collectors.joining(", ")));
    }

    private Statement findStatementById(UUID id) {
        return statementRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Statement with id %s wasn't found", id)));
    }

    private Client saveClient(LoanStatementRequestDto loanStatement) {

        var email = loanStatement.getEmail();
        Client client;

        if (clientRepository.existsByEmail(email)) {
            log.info("Client with email {} already exists", email);

            client = clientRepository.getClientByEmail(email);
            log.info("Existing client = {}", client);

            if (Objects.equals(client.getFirstName(), loanStatement.getFirstName()) &&
                    Objects.equals(client.getLastName(), loanStatement.getLastName()) &&
                    Objects.equals(client.getMiddleName(), loanStatement.getMiddleName()) &&
                    Objects.equals(client.getBirthdate(), loanStatement.getBirthdate()) &&
                    Objects.equals(client.getPassport().getPassportData().getSeries(), loanStatement.getPassportSeries()) &&
                    Objects.equals(client.getPassport().getPassportData().getNumber(), loanStatement.getPassportNumber())) {
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
}