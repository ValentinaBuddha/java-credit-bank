package ru.neoflex.deal.mapper;

import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.model.jsonb.PassportData;

public final class PassportMapper {

    public static Passport toEntity(String series, String number) {

        return Passport.builder()
                .passportData(new PassportData(series, number))
                .build();
    }

    private PassportMapper() {
    }
}
