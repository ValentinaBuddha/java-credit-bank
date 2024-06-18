package ru.neoflex.deal.service;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.model.Client;

public interface ClientService {

    Client saveClient(LoanStatementRequestDto loanStatement);

    void finishRegistration(Client client, FinishRegistrationRequestDto finishRegistration);
}
