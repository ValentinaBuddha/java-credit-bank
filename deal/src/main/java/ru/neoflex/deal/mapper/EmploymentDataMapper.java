package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.model.jsonb.EmploymentData;

@Mapper(componentModel = "spring")
public interface EmploymentDataMapper {

    @Mapping(target = "status", source = "employmentStatus")
    EmploymentData toEmploymentData(EmploymentDto employment);
}
