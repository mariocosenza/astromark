package it.astromark.attendance.controller;

import it.astromark.attendance.dto.JustifiableResponse;
import it.astromark.attendance.service.JustifiableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/students/")
public class JustifiableController {

    private final JustifiableService justifiableService;

    @Autowired
    public JustifiableController(JustifiableService justifiableService) {
        this.justifiableService = justifiableService;
    }

    @PatchMapping("{studentId}/justifiable/{justificationId}/justify")
    JustifiableResponse justify(@PathVariable UUID studentId, @PathVariable UUID justificationId, @RequestBody String justificationText, @RequestParam Boolean absence) {
        return justifiableService.justify(studentId, justificationId, justificationText, absence);
    }

    @GetMapping("{studentId}/year/{year}/justifiable/absences")
    List<JustifiableResponse> getAbsencesByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getAbsencesByYear(studentId, year);
    }

    @GetMapping("{studentId}/year/{year}/justifiable/delays")
    List<JustifiableResponse> getDelayByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getDelayByYear(studentId, year);
    }

    @GetMapping("{studentId}/year/{year}/justifiable/absences/total")
    Integer getTotalAbsences(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getTotalAbsences(studentId, year);
    }

    @GetMapping("{studentId}/year/{year}/justifiable/delays/total")
    Integer getTotalDelays(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getTotalDelays(studentId, year);
    }

}
