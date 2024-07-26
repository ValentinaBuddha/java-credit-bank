package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.deal.dto.StatementDtoShort;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Statement;
import ru.neoflex.deal.model.jsonb.AppliedOffer;
import ru.neoflex.deal.model.jsonb.StatementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.ChangeType.MANUAL;
import static ru.neoflex.deal.enums.CreditStatus.CALCULATED;
import static ru.neoflex.deal.enums.Status.DOCUMENT_CREATED;

@SpringBootTest(classes = {ru.neoflex.deal.mapper.StatementMapperImpl.class,
        ru.neoflex.deal.mapper.CreditMapperImpl.class,
        ru.neoflex.deal.mapper.PaymentScheduleMapperImpl.class,
        ru.neoflex.deal.mapper.ClientMapperImpl.class,
        ru.neoflex.deal.mapper.EmploymentMapperImpl.class,
        ru.neoflex.deal.mapper.PassportMapperImpl.class,
        ru.neoflex.deal.mapper.OfferMapperImpl.class})
class StatementMapperTest {

    @Autowired
    private StatementMapper statementMapper;

    private UUID id = UUID.fromString("6dd2ff79-5597-4c58-9a88-55ab84c9378d");
    private Client client = Client.builder()
            .firstName("Ivan")
            .lastName("Ivanov")
            .build();
    private BigDecimal rate = BigDecimal.valueOf(19);
    private BigDecimal amount = BigDecimal.valueOf(100000);
    private int term = 6;
    private BigDecimal monthlyPayment = BigDecimal.valueOf(17602.27);
    private BigDecimal psk = BigDecimal.valueOf(11.23);
    private LocalDateTime dateTime = LocalDateTime.now();
    private Credit credit = Credit.builder()
            .amount(amount)
            .term(term)
            .monthlyPayment(monthlyPayment)
            .rate(rate)
            .psk(psk)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .creditStatus(CALCULATED)
            .build();
    private Statement statement = Statement.builder()
            .id(id)
            .client(client)
            .credit(credit)
            .status(DOCUMENT_CREATED)
            .creationDate(dateTime)
            .statusHistory(new ArrayList<>())
            .sesCode("123456")
            .build();

    @Test
    void toListStatementDtoShort() {
        List<Statement> statements = List.of(statement);

        List<StatementDtoShort> mappedList = statementMapper.toListStatementDtoShort(statements);

        assertEquals(1, mappedList.size());
        assertEquals(id, mappedList.get(0).getId());
        assertEquals("Ivan", mappedList.get(0).getClient().getFirstName());
        assertEquals("Ivanov", mappedList.get(0).getClient().getLastName());
        assertNull(mappedList.get(0).getClient().getMiddleName());
        assertEquals(amount, mappedList.get(0).getCredit().getAmount());
        assertEquals(term, mappedList.get(0).getCredit().getTerm());
        assertEquals(monthlyPayment, mappedList.get(0).getCredit().getMonthlyPayment());
        assertEquals(rate, mappedList.get(0).getCredit().getRate());
        assertEquals(psk, mappedList.get(0).getCredit().getPsk());
        assertEquals(false, mappedList.get(0).getCredit().getIsInsuranceEnabled());
        assertEquals(false, mappedList.get(0).getCredit().getIsSalaryClient());
        assertEquals(CALCULATED, mappedList.get(0).getCredit().getCreditStatus());
        assertEquals(DOCUMENT_CREATED, mappedList.get(0).getStatus());
        assertEquals(dateTime, mappedList.get(0).getCreationDate());
        assertNull(mappedList.get(0).getSignDate());
    }

    @Test
    void toStatementDtoFull() {
        var appliedOffer = new AppliedOffer(id, amount, amount, 6, monthlyPayment, rate, false, false);
        statement.setAppliedOffer(appliedOffer);
        statement.setSignDate(dateTime);
        List<StatementStatus> history = new ArrayList<>(List.of(new StatementStatus(DOCUMENT_CREATED, dateTime, MANUAL)));
        statement.setStatusHistory(history);

        var mappedStatementDtoFull = statementMapper.toStatementDtoFull(statement);

        assertEquals(id, mappedStatementDtoFull.getId());
        assertEquals("Ivan", mappedStatementDtoFull.getClient().getFirstName());
        assertEquals("Ivanov", mappedStatementDtoFull.getClient().getLastName());
        assertEquals(amount, mappedStatementDtoFull.getCredit().getAmount());
        assertEquals(term, mappedStatementDtoFull.getCredit().getTerm());
        assertEquals(monthlyPayment, mappedStatementDtoFull.getCredit().getMonthlyPayment());
        assertEquals(rate, mappedStatementDtoFull.getCredit().getRate());
        assertEquals(psk, mappedStatementDtoFull.getCredit().getPsk());
        assertEquals(false, mappedStatementDtoFull.getCredit().getIsInsuranceEnabled());
        assertEquals(false, mappedStatementDtoFull.getCredit().getIsSalaryClient());
        assertEquals(DOCUMENT_CREATED, mappedStatementDtoFull.getStatus());
        assertEquals(dateTime, mappedStatementDtoFull.getCreationDate());
        assertEquals(dateTime, mappedStatementDtoFull.getSignDate());
        assertEquals("123456", mappedStatementDtoFull.getSesCode());
        assertEquals(1, mappedStatementDtoFull.getStatusHistory().size());
        assertEquals(DOCUMENT_CREATED, mappedStatementDtoFull.getStatusHistory().get(0).getStatus());
        assertEquals(dateTime, mappedStatementDtoFull.getStatusHistory().get(0).getTime());
        assertEquals(MANUAL, mappedStatementDtoFull.getStatusHistory().get(0).getChangeType());
    }

    @Test
    void toStatementDtoFull_withAppliedOffer() {
        var appliedOffer = new AppliedOffer(id, amount, amount, 6, monthlyPayment, rate, false, false);
        statement.setAppliedOffer(appliedOffer);

        var mappedStatementDtoFull = statementMapper.toStatementDtoFull(statement);

        assertEquals(id, mappedStatementDtoFull.getAppliedOffer().getStatementId());
        assertEquals(amount, mappedStatementDtoFull.getAppliedOffer().getRequestedAmount());
        assertEquals(amount, mappedStatementDtoFull.getAppliedOffer().getTotalAmount());
        assertEquals(monthlyPayment, mappedStatementDtoFull.getAppliedOffer().getMonthlyPayment());
        assertEquals(rate, mappedStatementDtoFull.getAppliedOffer().getRate());
        assertEquals(term, mappedStatementDtoFull.getAppliedOffer().getTerm());
        assertEquals(false, mappedStatementDtoFull.getAppliedOffer().getIsInsuranceEnabled());
        assertEquals(false, mappedStatementDtoFull.getAppliedOffer().getIsSalaryClient());
    }
}