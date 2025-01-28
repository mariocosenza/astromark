package it.astromark.user.parent.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.dto.ParentRequest;
import it.astromark.user.parent.service.ParentService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(
            summary = "Create a parent",
            description = "Creates a new parent account with the provided details."
    )
    @PostMapping("/create-parent")
    public ParentDetailedResponse create(@RequestBody @NotNull ParentRequest parentRequest) {
        log.info("Create parent: {}", parentRequest);
        return parentService.create(parentRequest);
    }


    @Operation(
            summary = "Retrieve associated students",
            description = "Gets a list of students associated with the logged-in parent."
    )
    @GetMapping("/students")
    public List<SchoolUserDetailed> getStudents() {
        return parentService.getStudents();
    }

    @Operation(
            summary = "Retrieve associated teachers",
            description = "Gets a list of teachers associated with the logged-in parent's children."
    )
    @GetMapping("/teachers")
    public List<SchoolUserResponse> getTeachers() {
        return parentService.getTeachers();
    }

}

