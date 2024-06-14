package ru.neoflex.deal.mapper;

import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Statement;

import java.time.LocalDateTime;

public final class StatementMapper {

    public static Statement toEntity(Client client) {

        return Statement.builder()
                .client(client)
                .creationDate(LocalDateTime.now())
                .build();
    }

    private StatementMapper() {
    }
}
