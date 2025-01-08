package it.astromark.classmanagement.mapper;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassManagementMapper {

    SchoolClassResponse toSchoolClassResponse(SchoolClass schoolClass);

    List<SchoolClassResponse> toSchoolClassResponseList(List<SchoolClass> marks);
}
