package it.astromark.user.teacher.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.entity.Teacher;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing teacher-related operations.
 * Provides CRUD functionality and additional methods for retrieving teacher-specific information.
 */
public interface TeacherService extends CrudService<Teacher, TeacherRequest, SchoolUserDetailed, UUID> {

    List<SchoolClassResponse> getSchoolClasses();
    List<String> getTeaching();
}
