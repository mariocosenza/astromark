package it.astromark.attendance.service;

import it.astromark.attendance.dto.JustifiableResponse;


import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface JustifiableService {
    JustifiableResponse justify(UUID studentId, UUID justificationId, String justificationText, Boolean absence);
    List<JustifiableResponse> getAbsencesByYear(UUID studentId, Year year);
    List<JustifiableResponse> getDelayByYear(UUID studentId, Year year);
    Integer getTotalAbsences(UUID studentId, Year year);
    Integer getTotalDelays(UUID studentId, Year year);
}
