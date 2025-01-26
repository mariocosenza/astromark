package it.astromark.user.parent.controller;


import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.dto.ParentRequest;
import it.astromark.user.parent.service.ParentService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping
    public ParentDetailedResponse create(@RequestBody @NotNull ParentRequest parentRequest) {
        return parentService.create(parentRequest);
    }

    @GetMapping("/students")
    public List<SchoolUserDetailed> getStudents() {
        return parentService.getStudents();
    }


    @GetMapping("/teachers")
    public List<SchoolUserResponse> getTeachers() {
        return parentService.getTeachers();
    }

}
