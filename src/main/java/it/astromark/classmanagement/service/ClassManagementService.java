package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.dto.SchoolClassStudentResponse;

import java.time.Year;
import java.util.List;

public interface ClassManagementService {

    /**
     * Retrieves the current school year.
     *
     * @return the current `Year` object
     * Pre-condition: None.
     * Post-condition: Returns the current school year as a `Year` object.
     */
    Year getYear();

    /**
     * Retrieves a list of all school classes.
     *
     * @return a list of `SchoolClassResponse` objects representing the school classes
     * Pre-condition: None.
     * Post-condition: Returns a list of all existing school classes.
     */
    List<SchoolClassResponse> getClasses();


    /**
     * Retrieves a list of students for a specific class.
     *
     * @param classId the ID of the class
     * @return a list of `SchoolClassStudentResponse` objects representing the students in the specified class
     * Pre-condition: The `classId` must not be null and must refer to an existing class.
     * Post-condition: Returns a list of students sorted by surname for the specified class.
     */
    List<SchoolClassStudentResponse> getStudents(Integer classId);
}
