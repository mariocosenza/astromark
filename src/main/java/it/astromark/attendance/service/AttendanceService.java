package it.astromark.attendance.service;

import it.astromark.attendance.dto.AttendanceRequest;
import it.astromark.attendance.dto.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing attendance-related operations.
 * Provides methods to retrieve and save attendance records for classes.
 */
public interface AttendanceService {

    /**
     * Retrieves the attendance records for a specific class on a given date.
     *
     * @param classId the ID of the class
     * @param date the date to retrieve attendance for
     * @return a list of AttendanceResponse objects representing the attendance records
     * Pre-condition: The classId and date must not be null. The class associated with the classId must exist.
     * Post-condition: Returns a list of attendance records for the specified class and date.
     */
    List<AttendanceResponse> getAttendance(Integer classId, LocalDate date);

    /**
     * Saves the attendance records for a specific class on a given date.
     *
     * @param classId the ID of the class
     * @param date the date to save attendance for
     * @param attendanceRequests a list of AttendanceRequest objects containing attendance details
     * Pre-condition: The classId and date must not be null. The attendanceRequests must not be null and must contain valid attendance data.
     * Post-condition: The attendance records are saved for the specified class and date.
     */
    void saveAttendance(Integer classId, LocalDate date, List<AttendanceRequest> attendanceRequests);
}

