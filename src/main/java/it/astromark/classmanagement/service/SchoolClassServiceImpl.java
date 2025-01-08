package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.mapper.SchoolClassMapper;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.user.teacher.entity.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SchoolClassServiceImpl implements SchoolClassService {

    private final TeacherClassRepository teacherClassRepository;
    private final SchoolClassMapper schoolClassMapper;

    @Autowired
    SchoolClassServiceImpl(TeacherClassRepository teacherClassRepository, SchoolClassMapper schoolClassMapper) {
        this.teacherClassRepository = teacherClassRepository;
        this.schoolClassMapper = schoolClassMapper;
    }

    @Override
    @PreAuthorize("hasRole('teacher')")
    @Transactional
    public List<SchoolClassResponse> getSchoolClassesByTeacher(Teacher teacher){
        return schoolClassMapper.toSchoolClassResponseList(teacherClassRepository.findByTeacher(teacher).stream().map(TeacherClass::getSchoolClass).toList());
    }

    @Override
    public SchoolClassResponse create(SchoolClass schoolClass) {
        return null;
    }

    @Override
    public SchoolClassResponse update(Integer integer, SchoolClass schoolClass) {
        return null;
    }

    @Override
    public SchoolClassResponse delete(Integer integer) {
        return null;
    }

    @Override
    public SchoolClass getById(Integer integer) {
        return null;
    }
}

