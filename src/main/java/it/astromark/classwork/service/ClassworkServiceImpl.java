package it.astromark.classwork.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.mapper.ClassworkMapper;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ClassworkServiceImpl implements ClassworkService {

    private final ClassActivityRepository classActivityRepository;
    private final AuthenticationService authenticationService;
    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;
    private final ClassworkMapper classworkMapper;

    @Autowired
    public ClassworkServiceImpl(ClassActivityRepository classActivityRepository, AuthenticationService authenticationService, SchoolUserService schoolUserService, StudentRepository studentRepository, ClassworkMapper classworkMapper) {
        this.classActivityRepository = classActivityRepository;
        this.authenticationService = authenticationService;
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classworkMapper = classworkMapper;
    }

    /*
    public void addChat(HomeworkID id) {

    }

    public void updateDescription(HomeworkID id, String description) {

    }

    public void updateDueDate(HomeworkID id, Date date) {

    }

    public HomeworkResponse addChat(HomeworkID id) {

    }

    */

    @Override
    @Transactional
    @PreAuthorize("hasRole('student') || hasRole('parent')")
    public List<ClassworkResponse> getClassActivities(Integer classId) {
        if(!schoolUserService.isLoggedParentStudentClass(classId)) {
            throw new AccessDeniedException("You are not allowed to access this class");
        } else if(!authenticationService.isStudent()) {
                if(studentRepository.findById(authenticationService.getStudent().orElseThrow().getId()).orElseThrow().getSchoolClasses().stream().noneMatch(schoolClass -> schoolClass.getId().equals(classId))) {
                    throw new AccessDeniedException("You are not allowed to access this class");
                }
        }

        return classworkMapper.classActivityToClassworkResponseList(classActivityRepository.findBySignedHourTeachingTimeslotClassTimetableSchoolClass_Id(classId)).stream().sorted(Comparator.comparing(s -> s.signedHour().date())).toList();
    }



}
