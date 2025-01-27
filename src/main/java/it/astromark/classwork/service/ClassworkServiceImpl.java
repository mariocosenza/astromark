package it.astromark.classwork.service;

import it.astromark.agenda.schoolclass.repository.SignedHourRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classwork.dto.ClassworkResponse;
import it.astromark.classwork.dto.HomeworkResponse;
import it.astromark.classwork.mapper.ClassworkMapper;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassworkServiceImpl implements ClassworkService {

    private static final String SCHOOL_CLASS_AUTHORIZATION_DENIED = "You are not allowed to access this class";
    private final ClassActivityRepository classActivityRepository;
    private final AuthenticationService authenticationService;
    private final SchoolUserService schoolUserService;
    private final StudentRepository studentRepository;
    private final ClassworkMapper classworkMapper;
    private final HomeworkRepository homeworkRepository;
    private final SignedHourRepository signedHourRepository;


    @Autowired
    public ClassworkServiceImpl(ClassActivityRepository classActivityRepository, AuthenticationService authenticationService, SchoolUserService schoolUserService, StudentRepository studentRepository, ClassworkMapper classworkMapper, HomeworkRepository homeworkRepository, SignedHourRepository signedHourRepository) {
        this.classActivityRepository = classActivityRepository;
        this.authenticationService = authenticationService;
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classworkMapper = classworkMapper;
        this.homeworkRepository = homeworkRepository;
        this.signedHourRepository = signedHourRepository;
    }

    public void updateDescription(Integer id, String description) {

    }

    public void updateDueDate(Integer id, LocalDate date) {

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<ClassworkResponse> getClassActivities(Integer classId) {
        if (!schoolUserService.isLoggedParentStudentClass(classId)) {
            throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            if (studentRepository.findById(authenticationService.getStudent().orElseThrow().getId()).orElseThrow().getSchoolClasses().stream().noneMatch(schoolClass -> schoolClass.getId().equals(classId))) {
                throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
            }
        }

        return classworkMapper.classActivityToClassworkResponseList(classActivityRepository.findBySignedHourTeachingTimeslotClassTimetableSchoolClass_Id(classId)).stream().sorted(Comparator.comparing(s -> s.signedHour().date())).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<HomeworkResponse> getHomework(Integer classId) {
        if (!schoolUserService.isLoggedParentStudentClass(classId)) {
            throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            if (studentRepository.findById(authenticationService.getStudent().orElseThrow().getId()).orElseThrow().getSchoolClasses().stream().noneMatch(schoolClass -> schoolClass.getId().equals(classId))) {
                throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
            }
        }

        return classworkMapper.homeworkToHomeworkResponseList(homeworkRepository.findAllBySignedHour_TeachingTimeslot_ClassTimetable_SchoolClass_Id(classId)).stream().sorted(Comparator.comparing(HomeworkResponse::dueDate)).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public Integer getSignedHourHomeworkId(Integer classId, Integer signedHourId) {
        if (!schoolUserService.isLoggedTeacherClass(classId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var signedHour = signedHourRepository.findById(signedHourId).orElseThrow();
        if (!signedHour.getTeacher().getId().equals(authenticationService.getTeacher().orElseThrow().getId())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var homework = homeworkRepository.findBySignedHour(signedHour);
        if (homework == null)
            return null;
        return homework.getId();
    }
}
