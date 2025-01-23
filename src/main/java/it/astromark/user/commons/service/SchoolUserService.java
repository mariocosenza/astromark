package it.astromark.user.commons.service;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.teacher.entity.Teacher;

import java.util.UUID;

public interface SchoolUserService {
    boolean isStudentParent(Parent parent, UUID studentId);

    boolean isLoggedUserParent(UUID studentId);

    boolean isLoggedTeacherClass(Integer classId);

    boolean isTeacherClass(Teacher teacher, Integer classId);

    boolean isLoggedParentStudentClass(Integer classId);

    SchoolUserResponse updatePreferences(SchoolUserUpdate schoolUserUpdate);

    SchoolUserResponse updateAddress(String address);

    boolean isLoggedTeacherStudent(UUID studentId);

    boolean isLoggedStudent(UUID studentId);

    SchoolUserDetailed getByIdDetailed();
}
