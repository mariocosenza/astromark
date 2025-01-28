package it.astromark.classwork.service;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.classwork.dto.*;

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
     * @param classId      the ID of the class
     * @param signedHourId the ID of the signed hour
     * @return the ID of the homework associated with the signed hour, or null if no homework is found
     * Pre-condition: The `classId` and `signedHourId` must not be null and must refer to existing entities.
     * The signed hour must belong to the authenticated teacher.
     * Post-condition: Returns the ID of the homework associated with the specified signed hour, or null if no homework exists.
     */
    Integer getSignedHourHomeworkId(Integer classId, Integer signedHourId);

    /**
     * Creates or updates a class activity based on the provided request and signed hour.
     *
     * @param request the `ClassActivityRequest` object containing the details of the class activity
     * @param signedHour the `SignedHour` object associated with the class activity
     * @return a `ClassActivityResponse` object representing the created or updated class activity
     * Pre-condition:
     * - The `request` and `signedHour` parameters must not be null.
     * - The `request.title()` must not be empty.
     * Post-condition: The created or updated class activity is saved in the repository.
     */
    ClassActivityResponse setActivity(ClassActivityRequest request, SignedHour signedHour);

    /**
     * Creates or updates a homework assignment based on the provided request and signed hour.
     *
     * @param request the `HomeworkRequest` object containing the details of the homework assignment
     * @param signedHour the `SignedHour` object associated with the homework assignment
     * @return a `HomeworkResponse` object representing the created or updated homework assignment
     * Pre-condition:
     * - The `request` and `signedHour` parameters must not be null.
     * - The `request.title()` must not be empty and `request.dueDate()` must be a future date.
     * Post-condition:
     * - The created or updated homework assignment is saved in the repository.
     * - If `request.hasChat()` is true, homework chats are created.
     */
    HomeworkResponse setHomework(HomeworkRequest request, SignedHour signedHour);

}
