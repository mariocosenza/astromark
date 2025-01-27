package it.astromark.classmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.classmanagement.dto.*;
import it.astromark.classmanagement.service.ClassManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Slf4j
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
            summary = "Retrieve classes for a specific teacher",
            description = "Gets a list of classes assigned to the teacher with the specified UUID."
    )
    @GetMapping("/{teacheruuid}/class")
    public List<SchoolClassResponse> getTeacherClasses(@PathVariable String teacheruuid) {
        return classManagementService.getTeacherClasses(UUID.fromString(teacheruuid));
    }

    @Operation(
            summary = "Add a teacher to a class",
            description = "Assigns a teacher with the specified UUID to a class based on the provided details."
    )
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

    @Operation(
            summary = "Retrieve all teachings",
            description = "Gets a list of all teachings managed by the class management system."
    )
    @GetMapping("/teaching")
    public List<TeachingResponse> getTeachings() {
        return classManagementService.getTeachings();
    }

    @Operation(
            summary = "Retrieve all subjects",
            description = "Gets a list of all subjects managed by the class management system."
    )
    @GetMapping("/subject")
    public List<String> getSubject() {
        return classManagementService.getSubject();
    }

    @Operation(
            summary = "Add a teaching",
            description = "Adds a new teaching for the teacher with the specified UUID based on the provided details."
    )
    @PostMapping("/{teacheruuid}/add-teaching")
    public void addTeaching(@PathVariable UUID teacheruuid, @RequestBody TeachingRequest teaching) {
        classManagementService.addTeaching(teacheruuid, teaching);
    }

    @Operation(
            summary = "Create a class",
            description = "Creates a new class based on the provided details and returns the created class."
    )
    @PostMapping("/class")
    public SchoolClassResponse create(@RequestBody SchoolClassRequest schoolClassRequest) {
        return classManagementService.schoolClassResponse(schoolClassRequest);
    }


    @Operation(
            summary = "Delete a class from a teacher",
            description = "Removes a class associated with a specific teacher using the teacher UUID and class ID."
    )
    @DeleteMapping("/{teacheruuid}/{schoolClassId}/delete-from-class")
    public void deleteFromClass(@PathVariable String teacheruuid, @PathVariable Integer schoolClassId) {
        classManagementService.removeClass(teacheruuid, schoolClassId);
    }

}
