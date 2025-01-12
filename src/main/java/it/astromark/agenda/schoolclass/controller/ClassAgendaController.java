package it.astromark.agenda.schoolclass.controller;

import it.astromark.agenda.schoolclass.dto.SignHourResponse;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;
import it.astromark.agenda.schoolclass.service.ClassAgendaService;
import it.astromark.agenda.schoolclass.service.SignedHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/classes")
public class ClassAgendaController {

    private final ClassAgendaService classAgendaService;
    private final SignedHourService signedHourService;

    @Autowired
    public ClassAgendaController(ClassAgendaService classAgendaService, SignedHourService signedHourService) {
        this.classAgendaService = classAgendaService;
        this.signedHourService = signedHourService;
    }

    @GetMapping("/{classId}/week-timeslots/{date}")
    public List<TeachingTimeslotResponse> getWeekTimeslots(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return classAgendaService.getWeekTimeslot(classId, date);
    }

    @GetMapping("/{classId}/signedHours/{date}")
    public List<SignHourResponse> getSignHours(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return signedHourService.getSignedHours(classId, date);
    }

}
