package it.astromark.user.student.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.service.StudentService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Retrieve student's school years",
            description = "Gets a list of years in which the specified student was enrolled."
    )
    @GetMapping("/{studentId}/years")
    public List<Integer> getStudentYears(@PathVariable UUID studentId) {
        log.info("Getting student years for student with id: {}", studentId);
        return studentService.getStudentYears(studentId);
    }

    @Operation(
            summary = "Retrieve student's classes by year",
            description = "Gets a list of classes for the specified student in a given year."
    )
    @GetMapping("/{studentId}/classes/{year}")
    public List<SchoolClassResponse> getSchoolClassByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return studentService.getSchoolClassByYear(studentId, year);
    }

    @Operation(
            summary = "Create a student",
            description = "Creates a new student account with the provided details."
    )
    @PostMapping("/create-student")
    public SchoolUserDetailed create(@RequestBody @NotNull StudentRequest studentRequest) {
        return studentService.create(studentRequest);
    }

    @Operation(
            summary = "Retrieve student details",
            description = "Gets detailed information for the specified student by their ID."
    )
    @GetMapping("/{studentId}")
    public SchoolUserDetailed getById(@PathVariable UUID studentId) {
        return studentService.getById(studentId);
    }

    @Operation(
            summary = "Retrieve student's attitude",
            description = "Gets the attitude description of the specified student by their ID."
    )
    @GetMapping("/{studentId}/attitude")
    public String attitude(@PathVariable UUID studentId) {
        return HtmlUtils.htmlEscape(studentService.attitude(studentId));
    }
}
