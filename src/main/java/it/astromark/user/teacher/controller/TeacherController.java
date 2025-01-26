package it.astromark.user.teacher.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.teacher.dto.TeacherDetailsResponse;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.dto.TeacherResponse;
import it.astromark.user.teacher.service.TeacherService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(
            summary = "Retrieve school classes",
            description = "Gets a list of all school classes associated with the logged-in teacher."
    )
    @GetMapping("/schoolClasses")
    public List<SchoolClassResponse> getSchoolClasses() {
        return teacherService.getSchoolClasses();
    }

    @Operation(
            summary = "Retrieve teaching subjects",
            description = "Gets a list of teaching subjects associated with the logged-in teacher."
    )
    @GetMapping("/teachings")
    public List<String> getTeachings() {
        return teacherService.getTeaching();
    }

    @Operation(
            summary = "Retrieve all teachers",
            description = "Gets a list of all teachers in the system."
    )
    @GetMapping("/all")
    public List<TeacherResponse> getTeachers() {
        return teacherService.getTeachers();
    }

    @Operation(
            summary = "Create a teacher account",
            description = "Creates a new teacher account with the provided details."
    )
    @PostMapping("/create")
    public SchoolUserDetailed create(@RequestBody @NotNull TeacherRequest teacherRequest) {
        return teacherService.create(teacherRequest);
    }

    @Operation(
            summary = "Retrieve teacher details",
            description = "Gets detailed teaching information for the specified teacher by UUID."
    )
    @GetMapping("/{teacheruuid}/teachings")
    public TeacherDetailsResponse getTeacher(@PathVariable String teacheruuid) {
        return teacherService.getTeacherTeaching(teacheruuid);
    }
}
