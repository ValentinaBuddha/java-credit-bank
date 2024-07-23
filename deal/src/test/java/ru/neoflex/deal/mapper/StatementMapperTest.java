package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.deal.dto.StatementDtoShort;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Statement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.neoflex.deal.enums.CreditStatus.CALCULATED;
import static ru.neoflex.deal.enums.Status.DOCUMENT_CREATED;

@SpringBootTest(classes = {ru.neoflex.deal.mapper.StatementMapperImpl.class,
        ru.neoflex.deal.mapper.CreditMapperImpl.class,
        ru.neoflex.deal.mapper.PaymentScheduleMapperImpl.class,
        ru.neoflex.deal.mapper.ClientMapperImpl.class})
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
    private LocalDateTime creationDate = LocalDateTime.now();
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
            .creationDate(creationDate)
            .statusHistory(new ArrayList<>())
            .sesCode("123456")
            .build();

    @Test
    void toStatementCreditDto() {
        var mappedStatementDtoForDossier = statementMapper.toStatementCreditDto(statement);

        assertEquals(id, mappedStatementDtoForDossier.getId());
        assertEquals(amount, mappedStatementDtoForDossier.getCredit().getAmount());
        assertEquals(term, mappedStatementDtoForDossier.getCredit().getTerm());
        assertEquals(monthlyPayment, mappedStatementDtoForDossier.getCredit().getMonthlyPayment());
        assertEquals(rate, mappedStatementDtoForDossier.getCredit().getRate());
        assertEquals(psk, mappedStatementDtoForDossier.getCredit().getPsk());
        assertEquals(false, mappedStatementDtoForDossier.getCredit().getIsInsuranceEnabled());
        assertEquals(false, mappedStatementDtoForDossier.getCredit().getIsSalaryClient());
        assertEquals("123456", mappedStatementDtoForDossier.getSesCode());
    }

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
        assertEquals(creationDate, mappedList.get(0).getCreationDate());
        assertNull(mappedList.get(0).getSignDate());
    }
}