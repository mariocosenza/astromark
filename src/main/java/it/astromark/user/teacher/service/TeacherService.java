package it.astromark.user.teacher.service;

import it.astromark.classmanagement.dto.TeacherClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.commons.service.CrudService;
import it.astromark.user.teacher.entity.Teacher;

import java.util.List;

public interface TeacherService extends CrudService<Teacher, Teacher, Teacher, Integer> {
    List<SchoolClass> getSchoolClasses();
}
