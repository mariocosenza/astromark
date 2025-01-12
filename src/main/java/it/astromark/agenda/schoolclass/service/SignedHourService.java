package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.schoolclass.dto.SignHourResponse;
import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.commons.service.CrudService;

import java.time.LocalDate;
import java.util.List;

public interface SignedHourService extends CrudService<SignedHour, SignedHour, SignedHour, Integer> {
    List<SignHourResponse> getSignedHours(Integer classId, LocalDate localDate);
}
