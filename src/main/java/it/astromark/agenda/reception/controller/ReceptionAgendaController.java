package it.astromark.agenda.reception.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.agenda.reception.dto.ReceptionBookingResponse;
import it.astromark.agenda.reception.dto.ReceptionTimeslotRequest;
import it.astromark.agenda.reception.dto.ReceptionTimeslotResponse;
import it.astromark.agenda.reception.entity.ReceptionBookingId;
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

    @Operation(summary = "Book a timeslot", description = "Allows booking of a reception timeslot by its ID. Returns true if booking is successful.")
    @PatchMapping("/timeslot/{id}/book")
    public boolean book(@PathVariable Integer id) {
        return receptionAgendaService.book(id);
    }


    @Operation(summary = "Confirm a booked reception timeslot", description = "Confirms a booked reception timeslot identified by its ID.")
    @PostMapping("/timeslot/confirm")
    public boolean confirm(@RequestBody ReceptionBookingId receptionTimeslotID) {
        return receptionAgendaService.confirm(receptionTimeslotID);
    }

    @Operation(summary = "Refuse a booked reception timeslot", description = "Refuses a booked reception timeslot identified by its ID.")
    @PostMapping("/timeslot/refuse")
    public boolean refuse(@RequestBody ReceptionBookingId receptionTimeslotID) {
        return receptionAgendaService.refuse(receptionTimeslotID);
    }

    @Operation(summary = "Add a new reception timeslot", description = "Creates a new reception timeslot based on the provided request and returns the details of the created timeslot.")
    @PostMapping("/timeslot/add")
    public ReceptionTimeslotResponse add(@RequestBody ReceptionTimeslotRequest request) {
        return receptionAgendaService.addTimeslot(request);
    }

    @Operation(summary = "Get booked timeslots", description = "Retrieves a list of all booked reception timeslots.")
    @GetMapping("/timeslot/booked")
    public List<ReceptionBookingResponse> getBookedSlots() {
        return receptionAgendaService.getBookedSlots();
    }

    @Operation(summary = "Get teacher's available timeslots", description = "Retrieves a list of available timeslots for a specific teacher identified by their UUID.")
    @GetMapping("/teacher/{teacherId}/timeslots")
    public List<ReceptionTimeslotResponse> getSlots(@PathVariable UUID teacherId) {
        return receptionAgendaService.getSlots(teacherId);
    }
}

