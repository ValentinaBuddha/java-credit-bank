package ru.neoflex.deal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.Status;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "status_history")
public class StatementStatusHistory {

    private Status status;

    private LocalDateTime time;

    private ChangeType changeType;

    private Long id;
}