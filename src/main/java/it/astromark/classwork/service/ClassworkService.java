package it.astromark.classwork.service;

import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;

import java.util.List;

/**
 * Service interface for managing classwork-related operations.
 * Provides methods to retrieve class activities and homework assignments.
 */
public interface ClassworkService {
    /**
     * Retrieves a list of class activities for a specific class.
     *
     * @param classId the ID of the class
     * @return a list of `ClassworkResponse` objects representing the class activities
     * Pre-condition: The `classId` must not be null and must refer to an existing class.
     * Post-condition: Returns a list of class activities associated with the specified class.
     */
    List<ClassworkResponse> getClassActivities(Integer classId);

    /**
     * Retrieves a list of homework assignments for a specific class.
     *
     * @param classId the ID of the class
     * @return a list of `HomeworkResponse` objects representing the homework assignments
     * Pre-condition: The `classId` must not be null and must refer to an existing class.
     * Post-condition: Returns a list of homework assignments associated with the specified class.
     */
    List<HomeworkResponse> getHomework(Integer classId);

    /**
     * Retrieves the ID of the homework associated with a specific signed hour for a class.
     *
     * @param classId the ID of the class
     * @param signedHourId the ID of the signed hour
     * @return the ID of the homework associated with the signed hour, or null if no homework is found
     * Pre-condition: The `classId` and `signedHourId` must not be null and must refer to existing entities.
     *                The signed hour must belong to the authenticated teacher.
     * Post-condition: Returns the ID of the homework associated with the specified signed hour, or null if no homework exists.
     */
    Integer getSignedHourHomeworkId(Integer classId, Integer signedHourId);

}
