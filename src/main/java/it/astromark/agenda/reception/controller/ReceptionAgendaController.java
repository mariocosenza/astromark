package it.astromark.agenda.reception.controller;

import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.service.ReceptionAgendaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/agenda/reception")
public class ReceptionAgendaController {

    private final ReceptionAgendaService receptionAgendaService;

    public ReceptionAgendaController(ReceptionAgendaService receptionAgendaService) {
        this.receptionAgendaService = receptionAgendaService;
    }

    @PatchMapping("/timeslot/{id}/book")
    public boolean book(@PathVariable Integer id) {
        return receptionAgendaService.book(id);
    }

    @GetMapping("/timeslot/booked")
    public List<ReceptionBookingResponse> getBookedSlots() {
        return receptionAgendaService.getBookedSlots();
    }

    @GetMapping("/teacher/{teacherId}/timeslots")
    public List<ReceptionTimeslotResponse> getSlots(@PathVariable UUID teacherId) {
        return receptionAgendaService.getSlots(teacherId);
    }


}
