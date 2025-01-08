package it.astromark.user.student.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.student.service.StudentService;
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
    public List<Year> getStudentYears(@PathVariable UUID studentId) {
        return studentService.getStudentYears(studentId);
    }


    @GetMapping("/{studentId}/classes/{year}")
    public List<SchoolClassResponse> getSchoolClassByYear(@PathVariable UUID studentId, @PathVariable Year year) {
        return studentService.getSchoolClassByYear(studentId, year);
    }

}
