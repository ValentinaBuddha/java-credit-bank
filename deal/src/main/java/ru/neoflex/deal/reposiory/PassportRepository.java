package ru.neoflex.deal.reposiory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Passport;

import java.util.UUID;

public interface PassportRepository extends JpaRepository<Passport, UUID> {
}
