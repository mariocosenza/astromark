package it.astromark.classwork.service;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.chat.service.HomeworkChatService;
import it.astromark.classwork.dto.*;
import it.astromark.classwork.entity.ClassActivity;
import it.astromark.classwork.entity.Homework;
import it.astromark.classwork.mapper.ClassworkMapper;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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
    private final HomeworkChatService homeworkChatService;

    @Autowired
    public ClassworkServiceImpl(ClassActivityRepository classActivityRepository, AuthenticationService authenticationService, SchoolUserService schoolUserService, StudentRepository studentRepository, ClassworkMapper classworkMapper, HomeworkRepository homeworkRepository, HomeworkChatService homeworkChatService) {
        this.classActivityRepository = classActivityRepository;
        this.authenticationService = authenticationService;
        this.schoolUserService = schoolUserService;
        this.studentRepository = studentRepository;
        this.classworkMapper = classworkMapper;
        this.homeworkRepository = homeworkRepository;
        this.homeworkChatService = homeworkChatService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<ClassworkResponse> getClassActivities(@NotNull Integer classId) {
        if (!schoolUserService.isLoggedParentStudentClass(classId)) {
            throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            if (studentRepository.findById(authenticationService.getStudent().orElseThrow().getId()).orElseThrow().getSchoolClasses().stream().noneMatch(schoolClass -> schoolClass.getId().equals(classId))) {
                throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
            }
        }

        return classworkMapper.classActivityToClassworkResponseList(classActivityRepository.findAllBySignedHour_TeachingTimeslot_ClassTimetable_SchoolClass_Id(classId)).stream().sorted(Comparator.comparing(s -> s.signedHour().date())).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<HomeworkResponse> getHomework(@NotNull Integer classId) {
        if (!schoolUserService.isLoggedParentStudentClass(classId)) {
            throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
        } else if (authenticationService.isStudent()) {
            if (studentRepository.findById(authenticationService.getStudent().orElseThrow().getId()).orElseThrow().getSchoolClasses().stream().noneMatch(schoolClass -> schoolClass.getId().equals(classId))) {
                throw new AccessDeniedException(SCHOOL_CLASS_AUTHORIZATION_DENIED);
            }
        }

        return classworkMapper.homeworkToHomeworkResponseList(homeworkRepository.findAllBySignedHour_TeachingTimeslot_ClassTimetable_SchoolClass_Id(classId))
                .stream()
                .sorted(Comparator.comparing(HomeworkResponse::dueDate).reversed())
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void createActivity(@NotNull ClassActivityRequest request, @NotNull  SignedHour signedHour) {
        if (!signedHour.getTeacher().getId().equals(authenticationService.getTeacher().orElseThrow().getId())
                || (request.title().isEmpty())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        ClassActivity activity;
        if (request.id() == null) {
            activity = ClassActivity.builder()
                    .signedHour(signedHour)
                    .title(request.title())
                    .description(request.description())
                    .build();
        } else {
            activity = classActivityRepository.findById(request.id()).orElseThrow();
            activity.setTitle(request.title());
            activity.setDescription(request.description());
        }

        classActivityRepository.save(activity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void createHomework(@NotNull HomeworkRequest request,@NotNull  SignedHour signedHour) {
        if (!signedHour.getTeacher().getId().equals(authenticationService.getTeacher().orElseThrow().getId())
                || (request.title().isEmpty()) || request.dueDate().isBefore(LocalDate.now())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        Homework homework;
        if (request.id() == null) {
            homework = Homework.builder()
                    .signedHour(signedHour)
                    .title(request.title())
                    .description(request.description())
                    .dueDate(request.dueDate())
                    .build();
        } else {
            homework = homeworkRepository.findById(request.id()).orElseThrow();
            homework.setTitle(request.title());
            homework.setDescription(request.description());
            homework.setDueDate(request.dueDate());
        }

        homeworkRepository.save(homework);

        if (request.hasChat()) {
            homeworkChatService.addChat(homework.getId());
        }
    }
}
