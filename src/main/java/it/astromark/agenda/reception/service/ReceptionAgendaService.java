package it.astromark.agenda.reception.service;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotRequest;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
import it.astromark.agenda.reception.entity.ReceptionTimetable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;


/**
 * Service interface for managing reception agendas and timeslots.
 * Provides methods for booking, confirming, refusing, and retrieving reception timeslots and bookings.
 */
public interface ReceptionAgendaService {
    /**
     * Books a reception timeslot for a given ID.
     *
     * @param receptionTimeslotID the ID of the reception timeslot to be booked
     * @return true if the timeslot is successfully booked, false otherwise
     * Pre-condition:
     * - The `receptionTimeslotID` must not be null.
     * - The timeslot associated with the given ID must exist and be available for booking.
     * Post-condition:
     * - The timeslot is marked as booked.
     * - Returns true if booking is successful, false if the timeslot is not available or does not exist.
     */
    boolean book(Integer receptionTimeslotID);

    /**
     * Confirms a previously booked reception timeslot.
     *
     * @param receptionTimeslotID the ID of the reception timeslot to confirm
     * @return true if the timeslot is successfully confirmed, false otherwise
     * Pre-condition:
     * - The `receptionTimeslotID` must not be null.
     * - The timeslot associated with the given ID must exist and have been booked.
     * Post-condition:
     * - The timeslot is marked as confirmed.
     * - Returns true if confirmation is successful, false otherwise.
     */
    boolean confirm(ReceptionBookingId receptionTimeslotID);

    /**
     * Refuses a reception timeslot for a given ID.
     *
     * @param receptionTimeslotID the ID of the reception timeslot to refuse
     * @return true if the timeslot is successfully refused, false otherwise
     * Pre-condition:
     * - The `receptionTimeslotID` must not be null.
     * - The timeslot associated with the given ID must exist and have been booked.
     * Post-condition:
     * - The timeslot is marked as refused.
     * - Returns true if refusal is successful, false otherwise.
     */
    boolean refuse(ReceptionBookingId receptionTimeslotID);

    /**
     * Adds a new reception timeslot.
     *
     * @param request the request containing timeslot details
     * @return the added `ReceptionTimeslotResponse` object
     * Pre-condition:
     * - The `request` must not be null and must contain valid timeslot details.
     * Post-condition:
     * - A new timeslot is added and returned as a response object.
     */
    ReceptionTimeslotResponse addTimeslot(ReceptionTimeslotRequest request);

    /**
     * Retrieves a list of not confirmed bookings for a given table.
     *
     * @param tableId the ID of the table to retrieve bookings for
     * @return a list of `ReceptionBookingResponse` objects representing not confirmed bookings
     * Pre-condition:
     * - The `tableId` must not be null.
     * Post-condition:
     * - Returns a list of bookings that are in a "not confirmed" state for the specified table.
     */
    List<ReceptionBookingResponse> getNotConfirmed(Integer tableId);

    /**
     * Retrieves a list of refused bookings for a given table.
     *
     * @param tableId the ID of the table to retrieve bookings for
     * @return a list of `ReceptionBookingResponse` objects representing refused bookings
     * Pre-condition:
     * - The `tableId` must not be null.
     * Post-condition:
     * - Returns a list of bookings that are in a "refused" state for the specified table.
     */
    List<ReceptionBookingResponse> getRefused(Integer tableId);

    /**
     * Retrieves a list of all booked reception slots.
     *
     * @return a list of `ReceptionBookingResponse` objects representing booked slots
     * Pre-condition:
     * - None.
     * Post-condition:
     * - Returns a list of all timeslots that are currently booked.
     */
    List<ReceptionBookingResponse> getBookedSlots();

    /**
     * Retrieves a list of all reception timeslots for a specific teacher.
     *
     * @param teacherID the UUID of the teacher
     * @return a list of `ReceptionTimeslotResponse` objects representing the teacher's timeslots
     * Pre-condition:
     * - The `teacherID` must not be null.
     * Post-condition:
     * - Returns a list of timeslots associated with the specified teacher.
     */
    List<ReceptionTimeslotResponse> getSlots(@NotNull UUID teacherID);

    /**
     * Creates a reception timetable for a specific teacher with the provided text information.
     *
     * @param teacherId the UUID of the teacher for whom the timetable is being created
     * @param textInfo  the text information about the reception timetable
     * @return the created `ReceptionTimetable` object
     * Pre-condition:
     * - The `teacherId` and `textInfo` must not be null.
     * - The teacher with the specified `teacherId` must exist.
     * Post-condition:
     * - A new reception timetable is created, associated with the specified teacher, and saved in the repository.
     * - The timetable includes the provided text information and has a start validity date of the current day.
     */
    ReceptionTimetable createReceptionTimetable(UUID teacherId, String textInfo);

}
