package it.astromark.attendance.service;

import it.astromark.attendance.dto.JustifiableResponse;

import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing justifications related to student absences and delays.
 * Provides methods to justify, retrieve, and calculate absences and delays.
 */
public interface JustifiableService {
    /**
     * Justifies an absence or delay for a student.
     *
     * @param studentId         the UUID of the student
     * @param justificationId   the UUID of the justification
     * @param justificationText the text explaining the justification
     * @param absence           true if justifying an absence, false if justifying a delay
     * @return a `JustifiableResponse` object representing the justification details
     * Pre-condition: The `studentId` and `justificationId` must not be null. The `justificationText` must not be null or empty.
     * Post-condition: A justification is created or updated, and a response object with the justification details is returned.
     */
    JustifiableResponse justify(UUID studentId, UUID justificationId, String justificationText, Boolean absence);

    /**
     * Retrieves a list of absences for a student in a specific year.
     *
     * @param studentId the UUID of the student
     * @param year      the year to filter absences
     * @return a list of `JustifiableResponse` objects representing the absences
     * Pre-condition: The `studentId` and `year` must not be null.
     * Post-condition: Returns a list of absences for the specified student and year.
     */
    List<JustifiableResponse> getAbsencesByYear(UUID studentId, Year year);

    /**
     * Retrieves a list of delays for a student in a specific year.
     *
     * @param studentId the UUID of the student
     * @param year      the year to filter delays
     * @return a list of `JustifiableResponse` objects representing the delays
     * Pre-condition: The `studentId` and `year` must not be null.
     * Post-condition: Returns a list of delays for the specified student and year.
     */
    List<JustifiableResponse> getDelayByYear(UUID studentId, Year year);

    /**
     * Retrieves the total number of absences for a student in a specific year.
     *
     * @param studentId the UUID of the student
     * @param year      the year to filter absences
     * @return the total number of absences as an integer
     * Pre-condition: The `studentId` and `year` must not be null.
     * Post-condition: Returns the total number of absences for the specified student and year.
     */
    Integer getTotalAbsences(UUID studentId, Year year);

    /**
     * Retrieves the total number of delays for a student in a specific year.
     *
     * @param studentId the UUID of the student
     * @param year      the year to filter delays
     * @return the total number of delays as an integer
     * Pre-condition: The `studentId` and `year` must not be null.
     * Post-condition: Returns the total number of delays for the specified student and year.
     */
    Integer getTotalDelays(UUID studentId, Year year);

}
