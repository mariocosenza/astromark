package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.schoolclass.dto.SignHourRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

import java.time.LocalDate;
import java.util.List;

public interface ClassAgendaService {
    /**
     * Retrieves the total teaching hours for a given class.
     *
     * @param classId the ID of the class
     * @return the total hours of teaching for the specified class
     * Pre-condition: The `classId` must not be null. The class associated with the `classId` must exist.
     * Post-condition: Returns the total teaching hours as an integer value.
     */
    Integer getTotalHour(Integer classId);

    /**
     * Adds a teaching timeslot for a given class.
     *
     * @param classId the ID of the class
     * @param request the request containing details about the teaching timeslot
     * Pre-condition: The `classId` must not be null. The `request` must not be null and must contain valid timeslot details.
     * Post-condition: A new teaching timeslot is added to the specified class.
     */
    void addTimeslot(Integer classId, TeachingTimeslotRequest request);

    /**
     * Signs the teaching hours for a given class.
     *
     * @param classId the ID of the class
     * @param request the request containing signing details
     * Pre-condition: The `classId` must not be null. The `request` must not be null and must contain valid signing details.
     * Post-condition: The specified hours are signed for the given class.
     */
    void sign(Integer classId, SignHourRequest request);

    /**
     * Checks if a timeslot is signed for a given ID and date.
     *
     * @param signedId the ID of the signed timeslot
     * @param date the date to check
     * @return true if the timeslot is signed, false otherwise
     * Pre-condition: The `signedId` and `date` must not be null. The timeslot associated with the `signedId` must exist.
     * Post-condition: Returns true if the timeslot is signed, otherwise false.
     */
    boolean isSigned(Integer signedId, LocalDate date);

    /**
     * Retrieves the teaching timeslots for a specific week.
     *
     * @param classID the ID of the class
     * @param date the date within the week to retrieve timeslots for
     * @return a list of `TeachingTimeslotResponse` objects representing the timeslots
     * Pre-condition: The `classID` and `date` must not be null. The class associated with the `classID` must exist.
     * Post-condition: Returns a list of timeslots for the specified week.
     */
    List<TeachingTimeslotResponse> getWeekTimeslot(Integer classID, LocalDate date);

    /**
     * Retrieves detailed teaching timeslots for a specific day.
     *
     * @param classId the ID of the class
     * @param localDate the date to retrieve timeslots for
     * @return a list of `TeachingTimeslotDetailedResponse` objects representing the detailed timeslots
     * Pre-condition: The `classId` and `localDate` must not be null. The class associated with the `classId` must exist.
     * Post-condition: Returns a list of detailed timeslots for the specified day.
     */
    List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(Integer classId, LocalDate localDate);

    /**
     * Creates a timetable for a given class.
     *
     * @param classId the ID of the class
     * @param request the request containing timetable details
     * Pre-condition: The `classId` must not be null. The `request` must not be null and must contain valid timetable details.
     * Post-condition: A new timetable is created for the specified class.
     */
    void createTimeTable(Integer classId, TeachingTimeslotRequest request);

}
