package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.schoolclass.dto.SignHourRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;

import java.time.LocalDate;
import java.util.List;

public interface ClassAgendaService {
    Integer getTotalHour(Integer classId);

    void addTimeslot(Integer classId, TeachingTimeslotRequest request);

    void sign(Integer classId, SignHourRequest request);

    boolean isSigned(Integer signedId, LocalDate date);

    List<TeachingTimeslotResponse> getWeekTimeslot(Integer classID, LocalDate date);

    List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(Integer classId, LocalDate localDate);
}
