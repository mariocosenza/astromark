package it.astromark.rating.service;

import it.astromark.rating.dto.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing mark-related operations.
 * Provides methods for retrieving, calculating, and managing marks, ratings, and reports.
 */
public interface MarkService {

    /**
     * Retrieves a list of marks for a specific student in a given year.
     *
     * @param studentId the UUID of the student
     * @param year the year to filter marks
     * @return a list of `MarkResponse` objects representing the marks of the student for the specified year
     * Pre-condition: The `studentId` and `year` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of marks for the specified student and year.
     */
    List<MarkResponse> getMarkByYear(UUID studentId, Year year);

    /**
     * Calculates the average mark for a specific student in a given year.
     *
     * @param studentId the UUID of the student
     * @param year the year to filter marks
     * @return a double representing the average mark of the student for the specified year
     * Pre-condition: The `studentId` and `year` must not be null. The student must have at least one mark in the specified year.
     * Post-condition: Returns the average mark for the specified student and year.
     */
    Double getAverage(UUID studentId, Year year);

    /**
     * Retrieves a semester report for a specific student and year.
     *
     * @param studentId the UUID of the student
     * @param year the year of the report, must be positive or zero
     * @param semester the semester of the report (true for first semester, false for second semester)
     * @return a `SemesterReportResponse` object representing the semester report
     * Pre-condition: The `studentId` and `year` must not be null. The `year` must be positive or zero. The student must exist.
     * Post-condition: Returns the semester report for the specified student, year, and semester.
     */
    SemesterReportResponse getReport(@NotNull UUID studentId, @PositiveOrZero Short year, Boolean semester);

    /**
     * Retrieves the details of a specific report by its ID.
     *
     * @param reportId the ID of the report
     * @return a `SemesterReportResponse` object representing the report details
     * Pre-condition: The `reportId` must not be null and must correspond to an existing report.
     * Post-condition: Returns the details of the specified report.
     */
    SemesterReportResponse viewReport(Integer reportId);

    /**
     * Retrieves ratings for a specific class, subject, and date.
     *
     * @param classId the ID of the class
     * @param subject the subject of the ratings
     * @param date the date to filter ratings
     * @return a list of `RatingsResponse` objects representing the ratings
     * Pre-condition: The `classId`, `subject`, and `date` must not be null. The class must exist.
     * Post-condition: Returns a list of ratings for the specified class, subject, and date.
     */
    List<RatingsResponse> getRatings(Integer classId, String subject, LocalDate date);

    /**
     * Retrieves all ratings for a specific class and subject.
     *
     * @param classId the ID of the class
     * @param subject the subject of the ratings
     * @return a list of `RatingsResponse` objects representing the ratings
     * Pre-condition: The `classId` and `subject` must not be null. The class must exist.
     * Post-condition: Returns a list of all ratings for the specified class and subject.
     */
    List<RatingsResponse> getEveryRatings(Integer classId, String subject);

    /**
     * Creates a new mark entry.
     *
     * @param mark the `MarkRequest` containing the details of the mark
     * @return a `MarkResponse` object representing the created mark
     * Pre-condition: The `mark` must not be null and must contain valid details.
     * Post-condition: A new mark entry is created and returned.
     */
    MarkResponse create(MarkRequest mark);

    /**
     * Updates an existing mark for a specific student.
     *
     * @param mark the `MarkUpdateRequest` containing the updated details of the mark
     * @param studentId the UUID of the student whose mark is being updated
     * @return a `MarkResponse` object representing the updated mark
     * Pre-condition: The `mark` and `studentId` must not be null. The student and mark must exist.
     * Post-condition: The specified mark is updated and returned.
     */
    MarkResponse update(MarkUpdateRequest mark, UUID studentId);

    /**
     * Deletes a mark by its ID.
     *
     * @param id the ID of the mark to be deleted
     * @return true if the mark is successfully deleted, false otherwise
     * Pre-condition: The `id` must not be null. The mark must exist.
     * Post-condition: The specified mark is deleted if it exists.
     */
    boolean delete(Integer id);
}
