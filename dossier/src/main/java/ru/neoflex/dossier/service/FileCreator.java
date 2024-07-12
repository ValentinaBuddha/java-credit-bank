package ru.neoflex.dossier.service;

import ru.neoflex.dossier.dto.CreditDto;

public interface FileCreator {

    void createTxtFile(CreditDto credit);
}
