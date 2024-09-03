package ru.neoflex.deal.reposiory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}