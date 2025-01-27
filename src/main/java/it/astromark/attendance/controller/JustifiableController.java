package it.astromark.attendance.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Justify a student's absence or delay",
            description = "Allows justification of an absence or delay for a specific student and justification ID."
    )
    @PatchMapping("{studentId}/justifiable/{justificationId}/justify")
    public JustifiableResponse justify(
            @PathVariable UUID studentId,
            @PathVariable UUID justificationId,
            @RequestBody String justificationText,
            @RequestParam Boolean absence
    ) {
        return justifiableService.justify(studentId, justificationId, justificationText, absence);
    }

    @Operation(
            summary = "Get absences by year",
            description = "Retrieves a list of justifiable absences for a specific student in a given year."
    )
    @GetMapping("{studentId}/year/{year}/justifiable/absences")
    public List<JustifiableResponse> getAbsencesByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getAbsencesByYear(studentId, year);
    }

    @Operation(
            summary = "Get delays by year",
            description = "Retrieves a list of justifiable delays for a specific student in a given year."
    )
    @GetMapping("{studentId}/year/{year}/justifiable/delays")
    public List<JustifiableResponse> getDelayByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getDelayByYear(studentId, year);
    }

    @Operation(
            summary = "Get total absences by year",
            description = "Retrieves the total number of justifiable absences for a specific student in a given year."
    )
    @GetMapping("{studentId}/year/{year}/justifiable/absences/total")
    public Integer getTotalAbsences(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getTotalAbsences(studentId, year);
    }

    @Operation(
            summary = "Get total delays by year",
            description = "Retrieves the total number of justifiable delays for a specific student in a given year."
    )
    @GetMapping("{studentId}/year/{year}/justifiable/delays/total")
    public Integer getTotalDelays(@PathVariable UUID studentId, @PathVariable Year year) {
        return justifiableService.getTotalDelays(studentId, year);
    }
}
