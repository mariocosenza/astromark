package it.astromark.rating.controller;

import it.astromark.rating.dto.*;
import it.astromark.rating.serivice.MarkService;
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


    @GetMapping("/{studentId}/marks/{year}")
    public List<MarkResponse> getMarkByYear(@PathVariable Year year, @PathVariable UUID studentId) {
        return markService.getMarkByYear(studentId, year);
    }

    @GetMapping("/{studentId}/marks/{year}/averages")
    public Double getAverage(@PathVariable Year year, @PathVariable UUID studentId) {
        return markService.getAverage(studentId, year);
    }

    @GetMapping("/{studentId}/reports/{year}")
    public SemesterReportResponse getReport(@PathVariable Short year, @PathVariable UUID studentId, @RequestParam Boolean firstSemester) {
        return markService.getReport(studentId, year, firstSemester);
    }

    @PatchMapping("/reports/{reportId}")
    public SemesterReportResponse viewReport(@PathVariable Integer reportId) {
        return markService.viewReport(reportId);
    }

    @GetMapping("classes/{classId}/ratings/{date}")
    public List<RatingsResponse> getRatings(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return markService.getRatings(classId, date);
    }

    @PostMapping("/marks")
    public MarkResponse create(@RequestBody MarkRequest mark) {
        return markService.create(mark);
    }

    @PatchMapping("/marks/{studentId}")
    public MarkResponse update(@RequestBody MarkUpdateRequest mark, @PathVariable UUID studentId) {
        return markService.update(mark, studentId);
    }

    @DeleteMapping("/marks/{id}")
    public boolean delete(@PathVariable Integer id) {
        return markService.delete(id);
    }
}


