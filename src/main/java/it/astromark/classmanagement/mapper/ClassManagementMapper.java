package it.astromark.classmanagement.mapper;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.dto.TeacherClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassManagementMapper {

    SchoolClassResponse toSchoolClassResponse(SchoolClass schoolClass);

    List<SchoolClassResponse> toSchoolClassResponseList(List<SchoolClass> marks);

    @Mapping(target = "description", source = "studyPlan.title")
    TeacherClassResponse toTeacherClassResponse(SchoolClass schoolClass);

    List<TeacherClassResponse> toTeacherClassResponseList(List<SchoolClass> schoolClasses);
}
