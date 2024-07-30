package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.dto.EmploymentDtoFull;
import ru.neoflex.deal.model.Employment;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    @Mapping(target = "employmentStatus", source = "employment.employmentData.status")
    @Mapping(target = "employerInn", source = "employment.employmentData.employerInn")
    @Mapping(target = "salary", source = "employment.employmentData.salary")
    @Mapping(target = "position", source = "employment.employmentData.position")
    @Mapping(target = "workExperienceTotal", source = "employment.employmentData.workExperienceTotal")
    @Mapping(target = "workExperienceCurrent", source = "employment.employmentData.workExperienceCurrent")
    EmploymentDtoFull toEmploymentDtoFull(Employment employment);
}
