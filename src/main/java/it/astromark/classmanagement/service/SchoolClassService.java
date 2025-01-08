package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.commons.service.CrudService;
import it.astromark.user.teacher.entity.Teacher;

import java.util.List;

public interface SchoolClassService extends CrudService<SchoolClass, SchoolClass, SchoolClassResponse, Integer> {
    List<SchoolClassResponse> getSchoolClassesByTeacher(Teacher teacher);
}
