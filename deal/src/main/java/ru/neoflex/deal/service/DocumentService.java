package ru.neoflex.deal.service;

public interface DocumentService {
    void sendDocuments(String statementId);

    void signDocuments(String statementId);

    void verifySesCode(String statementId, String sesCode);
}
