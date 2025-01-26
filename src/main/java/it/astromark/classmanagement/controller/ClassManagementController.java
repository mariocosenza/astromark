package it.astromark.classmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.classmanagement.dto.*;
import it.astromark.classmanagement.service.ClassManagementService;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{teacheruuid}/class")
    public List<SchoolClassResponse> getTeacherClasses(@PathVariable String teacheruuid) {
        return classManagementService.getTeacherClasses(UUID.fromString(teacheruuid));

    }

    @PostMapping("/{teacheruuid}/add-teacher-to-class")
    public void addTeacherToClass(@PathVariable String teacheruuid, @RequestBody AddToClassRequest addToClassRequest) {

        classManagementService.addTeacherToClass(UUID.fromString(teacheruuid), addToClassRequest);
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

    @GetMapping("/subject")
    public List<String> getSubject() {
        return classManagementService.getSubject();
    }

    @PostMapping("/{teacheruuid}/add-teaching")
    public void addTeaching(@PathVariable UUID teacheruuid, @RequestBody TeachingRequest teaching) {
        classManagementService.addTeaching(teacheruuid, teaching);
    }



    @PostMapping("/class")
    public SchoolClassResponse create(@RequestBody SchoolClassRequest schoolClassRequest) {
        return classManagementService.schoolClassResponse(schoolClassRequest);
    }
}
