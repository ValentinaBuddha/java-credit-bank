package ru.neoflex.deal.model.jsonb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.deal.enums.EmploymentStatus;
import ru.neoflex.deal.enums.Position;

import java.math.BigDecimal;

@Builder
@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentData {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
