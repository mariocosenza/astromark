package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing school classes, students, and teaching activities.
 * Provides methods for retrieving, adding, and managing class-related information.
 */
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
     * Retrieves a list of students in a specific class.
     *
     * @param classId the ID of the class
     * @return a list of `SchoolClassStudentResponse` objects representing the students in the class
     * Pre-condition: The `classId` must not be null. The class associated with the `classId` must exist.
     * Post-condition: Returns a list of students enrolled in the specified class.
     */
    List<SchoolClassStudentResponse> getStudents(Integer classId);

    /**
     * Retrieves a list of all teachings.
     *
     * @return a list of `TeachingResponse` objects representing all teachings
     * Pre-condition: None.
     * Post-condition: Returns a list of all teachings managed by the system.
     */
    List<TeachingResponse> getTeachings();

    /**
     * Creates or retrieves a school class based on the provided request.
     *
     * @param request the `SchoolClassRequest` containing details of the class
     * @return the created or retrieved `SchoolClassResponse` object
     * Pre-condition: The `request` must not be null and must contain valid class details.
     * Post-condition: Returns the created or existing school class details.
     */
    SchoolClassResponse schoolClassResponse(SchoolClassRequest request);

    /**
     * Adds a new teaching activity for a specific teacher.
     *
     * @param teacheruuid the UUID of the teacher
     * @param teaching    the `TeachingRequest` containing details of the teaching activity
     *                    Pre-condition: The `teacheruuid` and `teaching` must not be null. The teacher associated with the UUID must exist.
     *                    Post-condition: A new teaching activity is added for the specified teacher.
     */
    void addTeaching(UUID teacheruuid, TeachingRequest teaching);

    /**
     * Retrieves a list of all subjects.
     *
     * @return a list of strings representing all subjects
     * Pre-condition: None.
     * Post-condition: Returns a list of all subjects managed by the system.
     */
    List<String> getSubject();

    /**
     * Retrieves a list of classes assigned to a specific teacher.
     *
     * @param teacheruuid the UUID of the teacher
     * @return a list of `SchoolClassResponse` objects representing the teacher's classes
     * Pre-condition: The `teacheruuid` must not be null. The teacher associated with the UUID must exist.
     * Post-condition: Returns a list of classes assigned to the specified teacher.
     */
    List<SchoolClassResponse> getTeacherClasses(UUID teacheruuid);

    /**
     * Adds a teacher to a specific class.
     *
     * @param uuid              the UUID of the teacher
     * @param addToClassRequest the `AddToClassRequest` containing details of the class and the teacher's role
     *                          Pre-condition: The `uuid` and `addToClassRequest` must not be null. The teacher and class must exist.
     *                          Post-condition: The teacher is added to the specified class with the specified role.
     */
    void addTeacherToClass(UUID uuid, AddToClassRequest addToClassRequest);

    /**
     * Removes a class associated with a specific teacher.
     *
     * @param teacheruuid   the UUID of the teacher whose class is to be removed
     * @param schoolClassId the ID of the class to be removed
     *                      Pre-condition: The `teacheruuid` and `schoolClassId` must not be null. The teacher and class must exist.
     *                      Post-condition: The specified class is removed from the teacher's assignments.
     */
    void removeClass(String teacheruuid, Integer schoolClassId);

    /**
     * Retrieves the list of teachings associated with a specific class.
     *
     * @param classId the ID of the class for which teachings are to be retrieved
     * Pre-condition: The `classId` must not be null. The class must exist.
     * Post-condition: Returns a list of `TeachingResponse` objects containing details about the teachings of the specified class.
     *
     * @return a list of `TeachingResponse` objects representing the teachings of the class
     */
    List<TeachingResponse> getClassTeachings(Integer classId);

}
