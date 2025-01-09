package it.astromark.agenda.schoolclass.controller;

import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;
import it.astromark.agenda.schoolclass.service.ClassAgendaService;
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

    @Autowired
    public ClassAgendaController(ClassAgendaService classAgendaService) {
        this.classAgendaService = classAgendaService;
    }

    @GetMapping("/{classId}/week-timeslots/{date}")
    public List<TeachingTimeslotResponse> getWeekTimeslots(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return classAgendaService.getWeekTimeslot(classId, date);
    }



}
