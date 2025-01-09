package it.astromark.user.teacher.controller;

import it.astromark.classmanagement.dto.TeacherClassResponse;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.user.teacher.repository.TeacherRepository;
import it.astromark.user.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

}
