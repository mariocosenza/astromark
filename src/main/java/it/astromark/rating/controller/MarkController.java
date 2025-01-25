package it.astromark.rating.controller;

import it.astromark.rating.dto.*;
import it.astromark.rating.serivice.MarkService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class MarkController {

    private final MarkService markService;

    @Autowired
    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @Operation(
            summary = "Retrieve marks by year",
            description = "Gets a list of marks for a specific student in a given academic year."
    )
    @GetMapping("/{studentId}/marks/{year}")
    public List<MarkResponse> getMarkByYear(@PathVariable Year year, @PathVariable UUID studentId) {
        return markService.getMarkByYear(studentId, year);
    }

    @Operation(
            summary = "Retrieve average marks by year",
            description = "Gets the average of marks for a specific student in a given academic year."
    )
    @GetMapping("/{studentId}/marks/{year}/averages")
    public Double getAverage(@PathVariable Year year, @PathVariable UUID studentId) {
        return markService.getAverage(studentId, year);
    }

    @Operation(
            summary = "Retrieve semester report",
            description = "Gets the semester report for a specific student in a given academic year."
    )
    @GetMapping("/{studentId}/reports/{year}")
    public SemesterReportResponse getReport(@PathVariable Short year, @PathVariable UUID studentId, @RequestParam Boolean firstSemester) {
        return markService.getReport(studentId, year, firstSemester);
    }

    @Operation(
            summary = "Mark report as viewed",
            description = "Marks a specific semester report as viewed."
    )
    @PatchMapping("/reports/{reportId}")
    public SemesterReportResponse viewReport(@PathVariable Integer reportId) {
        return markService.viewReport(reportId);
    }

    @Operation(
            summary = "Retrieve ratings for a class on a specific date",
            description = "Gets a list of ratings for a specified class, teaching subject, and date."
    )
    @GetMapping("classes/{classId}/ratings/{teaching}/date/{date}")
    public List<RatingsResponse> getRatings(@PathVariable Integer classId, @PathVariable String teaching, @PathVariable LocalDate date) {
        return markService.getRatings(classId, teaching, date);
    }

    @Operation(
            summary = "Retrieve all ratings for a class",
            description = "Gets a complete list of ratings for a specified class and teaching subject, regardless of date."
    )
    @GetMapping("classes/{classId}/EveryRatings/{teaching}")
    public List<RatingsResponse> getEveryRatings(@PathVariable Integer classId, @PathVariable String teaching) {
        return markService.getEveryRatings(classId, teaching);
    }

    @Operation(
            summary = "Create a new mark",
            description = "Creates a new mark for a student."
    )
    @PostMapping("/marks")
    public MarkResponse create(@RequestBody MarkRequest mark) {
        return markService.create(mark);
    }

    @Operation(
            summary = "Update an existing mark",
            description = "Updates a specific mark for a student."
    )
    @PatchMapping("/marks/{studentId}")
    public MarkResponse update(@RequestBody MarkUpdateRequest mark, @PathVariable UUID studentId) {
        return markService.update(mark, studentId);
    }

    @Operation(
            summary = "Delete a mark",
            description = "Deletes a specific mark by its ID."
    )
    @DeleteMapping("/marks/{id}")
    public boolean delete(@PathVariable Integer id) {
        return markService.delete(id);
    }
}
