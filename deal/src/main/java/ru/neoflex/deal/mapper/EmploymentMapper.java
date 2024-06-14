package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.model.jsonb.EmploymentData;

public final class EmploymentMapper {

    public static EmploymentData toEntity(EmploymentDto employment) {

        return EmploymentData.builder()
                .status(employment.getEmploymentStatus())
                .employerINN(employment.getEmployerINN())
                .salary(employment.getSalary())
                .position(employment.getPosition())
                .workExperienceTotal(employment.getWorkExperienceTotal())
                .workExperienceCurrent(employment.getWorkExperienceCurrent())
                .build();
    }

    private EmploymentMapper() {
    }
}
