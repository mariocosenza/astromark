package it.astromark.user.teacher.service;

import it.astromark.classmanagement.entity.SchoolClass;
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

    /**
     * Retrieves a list of school classes associated with teachers.
     *
     * @return a list of `SchoolClass` objects representing the school classes
     * Pre-condition: None.
     * Post-condition: Returns a list of all school classes associated with teachers.
     */
    List<SchoolClass> getSchoolClasses();
}

