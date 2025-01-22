package it.astromark.user.student.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.entity.Student;

import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing student-related operations.
 * Extends the generic CrudService interface to provide basic CRUD functionality.
 */
public interface StudentService extends CrudService<Student, StudentRequest, SchoolUserDetailed, UUID> {

    /**
     * Retrieves a list of years associated with a specific student.
     *
     * @param studentId the UUID of the student
     * @return a list of integers representing the years the student has been enrolled
     * Pre-condition: The `studentId` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of years associated with the specified student.
     */
    List<Integer> getStudentYears(UUID studentId);

    /**
     * Retrieves the school classes associated with a specific student for a given year.
     *
     * @param studentId the UUID of the student
     * @param year the year to filter school classes
     * @return a list of `SchoolClassResponse` objects representing the classes of the student for the specified year
     * Pre-condition: The `studentId` and `year` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of school classes associated with the specified student and year.
     */
    List<SchoolClassResponse> getSchoolClassByYear(UUID studentId, Year year);
}

