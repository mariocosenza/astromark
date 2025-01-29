package it.astromark.user.teacher.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.teacher.dto.TeacherDetailsResponse;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.dto.TeacherResponse;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing teacher-related operations.
 * Provides CRUD functionality and additional methods for retrieving teacher-specific information.
 */
public interface TeacherService extends CrudService<Teacher, TeacherRequest, SchoolUserDetailed, UUID> {

    /**
     * Retrieves a list of school classes associated with the logged-in teacher.
     *
     * @return a list of `SchoolClassResponse` objects representing the school classes
     * Pre-condition: The logged-in teacher must exist and be associated with school classes.
     * Post-condition: Returns a list of school classes associated with the teacher.
     */
    List<SchoolClassResponse> getSchoolClasses();

    /**
     * Retrieves a list of teaching subjects associated with the logged-in teacher.
     *
     * @return a list of strings representing the subjects taught by the teacher
     * Pre-condition: The logged-in teacher must exist.
     * Post-condition: Returns a list of subjects associated with the teacher.
     */
    List<String> getTeaching();

    /**
     * Retrieves a list of all teachers in the system.
     *
     * @return a list of `TeacherResponse` objects representing all teachers
     * Pre-condition: None.
     * Post-condition: Returns a list of all teachers in the system.
     */
    List<TeacherResponse> getTeachers();

    /**
     * Retrieves detailed teaching information for a specific teacher.
     *
     * @param teacheruuid the UUID of the teacher as a string
     * @return a `TeacherDetailsResponse` object containing detailed information about the teacher's teachings
     * Pre-condition: The `teacheruuid` must not be null and must correspond to an existing teacher.
     * Post-condition: Returns detailed teaching information for the specified teacher.
     */
    TeacherDetailsResponse getTeacherTeaching(@NotEmpty String teacheruuid);
}
