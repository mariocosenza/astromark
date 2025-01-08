package it.astromark.rating.controller;

import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.serivice.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
