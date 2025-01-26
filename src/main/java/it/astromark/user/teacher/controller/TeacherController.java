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

import java.util.List;

@RestController
@RequestMapping("api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/schoolClasses")
    public List<SchoolClassResponse> getSchoolClasses() {
        return teacherService.getSchoolClasses();
    }

    @GetMapping("/teachings")
    public List<String> getTeachings() {
        return teacherService.getTeaching();
    }

    @GetMapping("/all")
    public List<TeacherResponse> getTeachers() {
        return teacherService.getTeachers();
    }

    @PostMapping("/create")
    public SchoolUserDetailed create(@RequestBody @NotNull TeacherRequest teacherRequest) {
        return teacherService.create(teacherRequest);
    }

    @GetMapping("/{teacheruuid}")
    public TeacherDetailsResponse getTeacher(@PathVariable String teacheruuid) {

        return teacherService.getTeacher(teacheruuid);
    }

}
