package it.astromark.user.teacher.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.dto.TeacherClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.user.teacher.entity.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherClassRepository teacherClassRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    TeacherServiceImpl(TeacherClassRepository teacherClassRepository, AuthenticationService authenticationService) {
        this.teacherClassRepository = teacherClassRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    @PreAuthorize("hasRole('teacher')")
    public List<SchoolClass> getSchoolClasses(){
        var teacher = authenticationService.getTeacher().orElseThrow();
        return teacherClassRepository.findByTeacher(teacher).stream()
                .map(TeacherClass::getSchoolClass)
                .sorted(Comparator.comparingInt(SchoolClass::getYear))
                .toList();
    }

    @Override
    public Teacher create(Teacher teacher) {
        return null;
    }

    @Override
    public Teacher update(Integer integer, Teacher teacher) {
        return null;
    }

    @Override
    public Teacher delete(Integer integer) {
        return null;
    }

    @Override
    public Teacher getById(Integer integer) {
        return null;
    }
}

