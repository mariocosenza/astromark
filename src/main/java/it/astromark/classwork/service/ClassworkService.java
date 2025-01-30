package it.astromark.classwork.service;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.classwork.dto.ClassActivityRequest;
import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkRequest;
import it.astromark.classwork.dto.HomeworkResponse;
import jakarta.validation.constraints.NotNull;

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
    List<ClassworkResponse> getClassActivities(@NotNull Integer classId);

    /**
     * Retrieves a list of homework assignments for a specific class.
     *
     * @param classId the ID of the class
     * @return a list of `HomeworkResponse` objects representing the homework assignments
     * Pre-condition: The `classId` must not be null and must refer to an existing class.
     * Post-condition: Returns a list of homework assignments associated with the specified class.
     */
    List<HomeworkResponse> getHomework(@NotNull Integer classId);

    /**
     * Creates or updates a class activity based on the provided request and signed hour.
     *
     * @param request    the `ClassActivityRequest` object containing the details of the class activity
     * @param signedHour the `SignedHour` object associated with the class activity
     *                   Pre-condition:
     *                   - The `request` and `signedHour` parameters must not be null.
     *                   - The `request.title()` must not be empty.
     *                   Post-condition: The created or updated class activity is saved in the repository.
     */
    void createActivity(@NotNull ClassActivityRequest request, @NotNull SignedHour signedHour);

    /**
     * Creates or updates a homework assignment based on the provided request and signed hour.
     *
     * @param request    the `HomeworkRequest` object containing the details of the homework assignment
     * @param signedHour the `SignedHour` object associated with the homework assignment
     *                   Pre-condition:
     *                   - The `request` and `signedHour` parameters must not be null.
     *                   - The `request.title()` must not be empty and `request.dueDate()` must be a future date.
     *                   Post-condition:
     *                   - The created or updated homework assignment is saved in the repository.
     *                   - If `request.hasChat()` is true, homework chats are created.
     */
    void createHomework(@NotNull HomeworkRequest request, @NotNull SignedHour signedHour);

}
