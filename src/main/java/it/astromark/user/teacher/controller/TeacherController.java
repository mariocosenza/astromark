package it.astromark.user.teacher.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.service.TeacherService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public SchoolUserDetailed create(@RequestBody @NotNull TeacherRequest teacherRequest) {
        return teacherService.create(teacherRequest);
    }

}
