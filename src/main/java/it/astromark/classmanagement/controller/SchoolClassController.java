package it.astromark.classmanagement.controller;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.service.SchoolClassService;
import it.astromark.user.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/teachers")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;
    private final TeacherRepository teacherRepository;

    @Autowired
    public SchoolClassController(SchoolClassService schoolClassService, TeacherRepository teacherRepository) {
        this.schoolClassService = schoolClassService;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/{teacherId}/schoolClasses")
    public List<SchoolClassResponse> getSchoolClassesByTeacher(@PathVariable UUID teacherId) {
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        return schoolClassService.getSchoolClassesByTeacher(teacher);
    }
}
