package it.astromark.user.teacher.controller;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.teacher.dto.TeacherRequest;
import it.astromark.user.teacher.service.TeacherService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import it.astromark.classmanagement.dto.TeacherClassResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final ClassManagementMapper classManagementMapper;

    @Autowired
    public TeacherController(TeacherService teacherService, ClassManagementMapper classManagementMapper) {
        this.teacherService = teacherService;
        this.classManagementMapper = classManagementMapper;
    }

    @GetMapping("/schoolClasses")
    @Transactional
    public List<TeacherClassResponse> getSchoolClasses() {
        return classManagementMapper.toTeacherClassResponseList(teacherService.getSchoolClasses());
    }

    @PostMapping
    public SchoolUserDetailed create(@RequestBody @NotNull TeacherRequest teacherRequest) {
        return teacherService.create(teacherRequest);
    }

}
