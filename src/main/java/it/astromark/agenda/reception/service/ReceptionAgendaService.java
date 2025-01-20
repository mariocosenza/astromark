package it.astromark.agenda.reception.service;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotRequest;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ReceptionAgendaService {
    boolean book(Integer receptionTimeslotID);
    boolean confirm(Integer receptionTimeslotID);
    boolean refuse(Integer receptionTimeslotID);
    ReceptionTimeslotResponse addTimeslot(Integer id, ReceptionTimeslotRequest request);
    List<ReceptionBookingResponse> getNotConfirmed(Integer tableId);
    List<ReceptionBookingResponse> getRefused(Integer tableId);
    List<ReceptionBookingResponse> getBookedSlots();
    Page<ReceptionTimeslotResponse> getSlots(@NotNull UUID teacherID, @NotNull Integer pageNo, @NotNull Integer pageSize);
}
