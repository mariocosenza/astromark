package it.astromark.user.student.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.service.StudentService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{studentId}/years")
    public List<Integer> getStudentYears(@PathVariable UUID studentId) {
        log.info("Getting student years for student with id: {}", studentService.getStudentYears(studentId));
        return studentService.getStudentYears(studentId);
    }

    @GetMapping("/{studentId}/classes/{year}")
    public List<SchoolClassResponse> getSchoolClassByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return studentService.getSchoolClassByYear(studentId, year);
    }

    @PostMapping
    public SchoolUserDetailed create(@RequestBody @NotNull StudentRequest studentRequest) {
        return studentService.create(studentRequest);
    }

    @GetMapping("/{studentId}")
    public SchoolUserDetailed getById(@PathVariable UUID studentId) {
        return studentService.getById(studentId);
    }

    @GetMapping("/{studentId}/attitude")
    public String attitude(@PathVariable UUID studentId) {
        return studentService.attitude(studentId);
    }
}
