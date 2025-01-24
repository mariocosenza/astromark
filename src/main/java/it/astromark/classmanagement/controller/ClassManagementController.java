package it.astromark.classmanagement.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.service.ClassManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("api/class-management")
public class ClassManagementController {

    private final ClassManagementService classManagementService;

    public ClassManagementController(ClassManagementService classManagementService) {
        this.classManagementService = classManagementService;
    }

    @Operation(
            summary = "Retrieve the current year",
            description = "Gets the current academic year managed by the class management system."
    )
    @GetMapping("/year")
    public Year getYear() {
        return classManagementService.getYear();
    }

    @Operation(
            summary = "Retrieve all classes",
            description = "Gets a list of all classes managed by the class management system."
    )
    @GetMapping("/all")
    public List<SchoolClassResponse> getClasses() {
        return classManagementService.getClasses();
    }
}
