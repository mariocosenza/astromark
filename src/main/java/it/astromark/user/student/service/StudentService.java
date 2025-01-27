package it.astromark.user.student.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    /**
     * Retrieves a list of years associated with a specific student.
     *
     * @param studentId the UUID of the student
     * @return a list of integers representing the years the student has been enrolled
     * Pre-condition: The `studentId` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of years associated with the specified student.
     */
    List<Integer> getStudentYears(@NotNull UUID studentId);
    /**
     * Retrieves a list of school classes for a specific student in a given year.
     *
     * @param studentId The unique identifier of the student (must not be null)
     * @param year The academic year to retrieve classes for (must be in the past or present)
     * @return A list of {@link SchoolClassResponse} objects representing the student's classes
     * @throws IllegalArgumentException if the student ID is not found
     * @throws ValidationException if the input parameters do not meet validation constraints
     */
    List<SchoolClassResponse> getSchoolClassByYear(@NotNull UUID studentId, @PastOrPresent Year year);

    /**
     * Retrieves detailed information about a student by their unique identifier.
     *
     * @param studentId The unique identifier of the student to retrieve (must not be null)
     * @return A {@link SchoolUserDetailed} object containing comprehensive student information
     * @throws EntityNotFoundException if no student is found with the given ID
     */
    SchoolUserDetailed getById(@NotNull UUID studentId);

    /**
     * Creates a new student record in the system.
     *
     * @param studentRequest The request object containing student creation details (must not be null)
     * @return A {@link SchoolUserDetailed} object representing the newly created student
     * @throws ValidationException if the student request fails validation
     */
    SchoolUserDetailed create(@NotNull StudentRequest studentRequest);

    /**
     * Retrieves the attitude or behavioral assessment of a student.
     *
     * @param studentId The unique identifier of the student (must not be null)
     * @return A {@link String} representing the student's current attitude or behavioral status
     * @throws EntityNotFoundException if no student is found with the given ID
     */
    String attitude(@NotNull UUID studentId);
}
