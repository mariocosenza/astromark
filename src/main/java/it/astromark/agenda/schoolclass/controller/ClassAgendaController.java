package it.astromark.agenda.schoolclass.controller;

import it.astromark.agenda.schoolclass.dto.*;
import it.astromark.agenda.schoolclass.service.ClassAgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/classes")
public class ClassAgendaController {

    private final ClassAgendaService classAgendaService;

    @Autowired
    public ClassAgendaController(ClassAgendaService classAgendaService) {
        this.classAgendaService = classAgendaService;
    }

    @Operation(summary = "Retrieve weekly timeslots", description = "Gets all teaching timeslots for the specified class and week starting from the given date.")
    @GetMapping("/{classId}/week-timeslots/{date}")
    public List<TeachingTimeslotResponse> getWeekTimeslots(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return classAgendaService.getWeekTimeslot(classId, date);
    }

    @Operation(summary = "Retrieve detailed teaching timeslots", description = "Gets detailed teaching timeslot information for the specified class and date.")
    @GetMapping("/{classId}/signedHours/{date}")
    public List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return classAgendaService.getTeachingTimeslot(classId, date);
    }

    @Operation(summary = "Sign teaching hour", description = "Signs a teaching hour for the specified class with the provided details.")
    @PostMapping("/{classId}/signHour")
    public TeachingTimeslotDetailedResponse sign(@PathVariable Integer classId, @RequestBody SignHourRequest signHourRequest) {
        return classAgendaService.sign(classId, signHourRequest);
    }

    @Operation(summary = "Create class timetable", description = "Creates a timetable for the class based on the provided request details.")
    @PostMapping("/createTimeTable")
    public void createTimeTable(@RequestBody ClassTimeTableRequest classTimeTableRequest) {
        classAgendaService.createTimeTable(classTimeTableRequest);
    }

    @Operation(summary = "Create a teaching timeslot", description = "Creates a new teaching timeslot for the specified class with the provided details.")
    @PostMapping("/{classId}/createTimeSlot")
    public void createTimeSlot(@PathVariable Integer classId, @RequestBody TeachingTimeslotRequest teachingTimeslotRequest) {
        classAgendaService.addTimeslot(classId, teachingTimeslotRequest);
    }

    @Operation(summary = "Retrieve class schedule", description = "Gets the current schedule for the specified class.")
    @GetMapping("/{classId}/class-schedule")
    public List<TeachingTimeslotResponse> classSchedule(@PathVariable Integer classId) {
        return classAgendaService.getClassTimeslot(classId, LocalDate.now());
    }

    @Operation(summary = "Retrieve class timetable", description = "Gets the complete timetable for the specified class.")
    @GetMapping("/{classId}/timetable")
    public List<TimeTableResponse> getTimeTable(@PathVariable Integer classId) {
        return classAgendaService.getTimeTable(classId);
    }
}
