package ru.neoflex.deal.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.neoflex.deal.model.jsonb.PassportData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "passport_id")
    private UUID id;

    @Type(type = "json")
    private PassportData passportData;

    public Passport(PassportData passportData) {
        this.passportData = passportData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passport passport)) return false;
        return getId().equals(passport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}