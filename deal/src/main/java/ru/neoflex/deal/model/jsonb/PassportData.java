package ru.neoflex.deal.model.jsonb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PassportData {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

    public PassportData(String series, String number) {
        this.series = series;
        this.number = number;
    }
}
