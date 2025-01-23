package it.astromark.rating.serivice;

import it.astromark.rating.dto.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing mark-related operations.
 */
public interface MarkService  {
    /**
     * Retrieves a list of marks for a specific student in a given year.
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

    List<RatingsResponse> getRatings(Integer classId, LocalDate date);

    MarkResponse create(MarkRequest mark);

    MarkResponse update(MarkUpdateRequest mark, UUID studentId);

    boolean delete(Integer id);
}
