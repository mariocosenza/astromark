package it.astromark.user.commons.mapper;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SchoolUserMapper {
    SchoolUserResponse toSchoolUserResponse(SchoolUser student);
    SchoolUserDetailed toSchoolUserDetailed(SchoolUser student);
    ParentDetailedResponse toParentDetailedResponse(Parent student);
    List<SchoolUserDetailed> toSchoolUserDetailedList(Set<Student> students);
    List<SchoolUserResponse> toSchoolUserResponseList(List<Teacher> teachers);

}
