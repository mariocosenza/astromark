package it.astromark.classmanagement.controller;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.dto.SchoolClassStudentResponse;
import it.astromark.classmanagement.dto.TeachingResponse;
import it.astromark.classmanagement.service.ClassManagementService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(
            summary = "Retrieve students for a specific class",
            description = "Gets a list of students enrolled in a specific class."
    )
    @GetMapping("/{classId}/students")
    public List<SchoolClassStudentResponse> getStudents(@PathVariable Integer classId) {
        return classManagementService.getStudents(classId);
    }

    @GetMapping("/teaching")
    public List<TeachingResponse> getTeachings() {
        return classManagementService.getTeachings();
    }
}
