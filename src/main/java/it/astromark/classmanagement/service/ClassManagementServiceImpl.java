package it.astromark.classmanagement.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.entity.Subject;
import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.classmanagement.didactic.repository.SubjectRepository;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.dto.*;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.classmanagement.mapper.ClassManagementMapper;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    private final TeacherClassRepository teacherClassRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final AuthenticationService authenticationService;
    private final ClassManagementMapper classManagementMapper;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolUserService schoolUserService;
    private final TeachingRepository teachingRepository;

    public ClassManagementServiceImpl(TeacherClassRepository teacherClassRepository, SubjectRepository subjectRepository, TeacherRepository teacherRepository, AuthenticationService authenticationService, ClassManagementMapper classManagementMapper, SchoolClassRepository schoolClassRepository, SchoolUserService schoolUserService, TeachingRepository teachingRepository) {
        this.teacherClassRepository = teacherClassRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.authenticationService = authenticationService;
        this.classManagementMapper = classManagementMapper;
        this.schoolClassRepository = schoolClassRepository;
        this.schoolUserService = schoolUserService;
        this.teachingRepository = teachingRepository;
    }

    @Override
    public Year getYear() {
        var month = LocalDate.now().getMonthValue();
        if (month <= 9) {
            return Year.of(LocalDate.now().getYear());
        } else {
            return Year.of(LocalDate.now().getYear() - 1);
        }
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY') || hasRole('TEACHER')")
    @Transactional
    public List<SchoolClassResponse> getClasses() {
        SchoolUser user;
        if (authenticationService.getSecretary().isPresent()) {
            user = authenticationService.getSecretary().get();
        } else {
            user = authenticationService.getTeacher().orElseThrow();
        }

        return classManagementMapper.toSchoolClassResponseList(schoolClassRepository.findBySchool(user.getSchool()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SECRETARY') || hasRole('TEACHER')")
    public List<SchoolClassStudentResponse> getStudents(Integer classId) {
        if (!schoolUserService.isLoggedTeacherClass(classId)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var students = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + classId))
                .getStudents()
                .stream()
                .sorted(Comparator.comparing(SchoolUser::getSurname))
                .toList();


        return classManagementMapper.toSchoolClassStudentResponseList(students);
    }

    @Override
    @Transactional
    public List<TeachingResponse> getTeachings() {
        return teachingRepository.findAll().stream()
                .map(t -> new TeachingResponse(
                        t.getTeacher().getUsername(),
                        t.getSubjectTitle().getTitle()
                ))
                .limit(20)
                .toList();
    }

    @Override
    public void addTeaching(UUID teacheruuid, TeachingRequest teachingRequest) {
        var teacher = teacherRepository.findById(teacheruuid)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found for UUID: " + teacheruuid));


        var teachingId = new TeachingId(teacher.getId(), teachingRequest.subjectTitle());

        var subject = subjectRepository.findAll().stream().filter(s -> s.getTitle().equals(teachingRequest.subjectTitle())).toList().getFirst();

        var teaching = Teaching.builder()
                .id(teachingId)
                .teacher(teacher)
                .subjectTitle(subject)
                .typeOfActivity(teachingRequest.activityType())
                .build();

        teachingRepository.save(teaching);
    }

    @Override
    @Transactional
    public List<String> getSubject() {
        return subjectRepository.findAll().stream().map(Subject::getTitle).toList();
    }

    @Override
    @Transactional
    public List<SchoolClassResponse> getTeacherClasses(UUID teacherId) {

        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        var classes = teacherClassRepository.findByTeacher(teacher).stream().map(TeacherClass::getSchoolClass).toList();

        return classes.stream().map(c -> new SchoolClassResponse(
                c.getId(),
                c.getYear(),
                c.getLetter(),
                c.getNumber(),
                ""
        )).toList();

    }

    @Override
    @Transactional
    public void addTeacherToClass(UUID uuid, AddToClassRequest addToClassRequest) {

        System.out.println(uuid + " " + addToClassRequest);

        var teacher = teacherRepository.findById(uuid).orElseThrow();
        var teacherClass = schoolClassRepository.findById(addToClassRequest.classId()).orElseThrow();
        var teacherClassId = new TeacherClassId(teacher.getId(), teacherClass.getId());
        teacherClassRepository.save(new TeacherClass(
                teacherClassId,
                teacher,
                teacherClass,
                addToClassRequest.isCoordinator()

        ));
    }


}
