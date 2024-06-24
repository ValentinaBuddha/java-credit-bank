package ru.neoflex.deal.reposiory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Client;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByEmail(String email);

    Client getClientByEmail(String email);
}