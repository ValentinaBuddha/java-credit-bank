package ru.neoflex.deal.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.neoflex.deal.enums.CreditStatus;
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "credit")
public class Credit {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "credit_id")
    private UUID id;

    private BigDecimal amount;

    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    @Basic(fetch = FetchType.LAZY)
    @Type(type = "json")
    @Column(name = "payment_schedule")
    private List<PaymentScheduleElement> paymentSchedule;

    @Column(name = "insurance_enabled")
    private Boolean isInsuranceEnabled;

    @Column(name = "salary_client")
    private Boolean isSalaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credit credit)) return false;
        return getId().equals(credit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
