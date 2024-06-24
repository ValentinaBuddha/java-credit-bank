package ru.neoflex.deal.model.jsonb;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.Status;

import java.time.LocalDateTime;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatus {
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;
}