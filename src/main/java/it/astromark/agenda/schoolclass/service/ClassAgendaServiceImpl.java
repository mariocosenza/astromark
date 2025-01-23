package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.commons.mapper.TimeslotMapper;
import it.astromark.agenda.schoolclass.dto.SignHourRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;
import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.agenda.schoolclass.mapper.ClassAgendaMapper;
import it.astromark.agenda.schoolclass.repository.ClassTimetableRepository;
import it.astromark.agenda.schoolclass.repository.SignedHourRepository;
import it.astromark.agenda.schoolclass.repository.TeachingTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.classwork.entity.ClassActivity;
import it.astromark.classwork.entity.Homework;
import it.astromark.classwork.repository.ClassActivityRepository;
import it.astromark.classwork.repository.HomeworkRepository;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
public class ClassAgendaServiceImpl implements ClassAgendaService {

    private final TeachingTimeslotRepository teachingTimeslotRepository;
    private final TimeslotMapper timeslotMapper;
    private final SchoolUserService schoolUserService;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final ClassAgendaMapper classAgendaMapper;
    private final ClassActivityRepository classActivityRepository;
    private final HomeworkRepository homeworkRepository;
    private final SignedHourRepository signedHourRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final ClassTimetableRepository classTimetableRepository;

    @Autowired
    public ClassAgendaServiceImpl(TeachingTimeslotRepository teachingTimeslotRepository, TimeslotMapper timeslotMapper, SchoolUserService schoolUserService, AuthenticationService authenticationService, StudentRepository studentRepository, ClassAgendaMapper classAgendaMapper, ClassActivityRepository classActivityRepository, HomeworkRepository homeworkRepository, SignedHourRepository signedHourRepository, TeacherClassRepository teacherClassRepository, ClassTimetableRepository classTimetableRepository) {
        this.teachingTimeslotRepository = teachingTimeslotRepository;
        this.timeslotMapper = timeslotMapper;
        this.schoolUserService = schoolUserService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.classAgendaMapper = classAgendaMapper;
        this.classActivityRepository = classActivityRepository;
        this.homeworkRepository = homeworkRepository;
        this.signedHourRepository = signedHourRepository;
        this.teacherClassRepository = teacherClassRepository;
        this.classTimetableRepository = classTimetableRepository;
    }


    @Override
    public Integer getTotalHour(Integer classId) {
        return 0;
    }

    @Override
    public void addTimeslot(Integer classId, TeachingTimeslotRequest request) {

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void sign(Integer classId, SignHourRequest request) {
        var teacher = authenticationService.getTeacher().orElseThrow();
        if (teacherClassRepository.findByTeacher(teacher).stream()
                .noneMatch(c -> c.getSchoolClass().getId().equals(classId))) {
            throw new AccessDeniedException("You are not allowed to sign this class");
        }

        var signedhour = signedHourRepository.findById(request.slotId()).orElse(null);

        ClassActivity activity = null;
        Homework homework = null;

        if (signedhour != null) {
            if (!signedhour.getTeacher().getId().equals(teacher.getId())) {
                throw new AccessDeniedException("You are not allowed to edit this hour");
            }

            activity = classActivityRepository.findBySignedHour(signedhour);
            homework = homeworkRepository.findBySignedHour(signedhour);
        } else {
            var slot = teachingTimeslotRepository.findById(request.slotId()).orElseThrow();
            if (!slot.getTeaching().getTeacher().getId().equals(teacher.getId())) {
                throw new AccessDeniedException("You are not allowed to sign this hour");
            }

            signedhour = new SignedHour();
            signedhour.setTeachingTimeslot(slot);
            signedhour.setTeacher(teacher);
            signedhour.setTimeSign(Instant.now());

            signedHourRepository.save(signedhour);
        }

        if (!request.activityTitle().isEmpty()) {
            if (activity == null){
                activity = new ClassActivity();
                activity.setSignedHour(signedhour);
            }

            activity.setTitle(request.activityTitle());
            activity.setDescription(request.activityDescription());

            classActivityRepository.save(activity);
        }

        if (!request.homeworkTitle().isEmpty()) {
            if (homework == null){
                homework = new Homework();
                homework.setSignedHour(signedhour);
            }

            homework.setTitle(request.homeworkTitle());
            homework.setDescription(request.homeworkDescription());
            homework.setDueDate(LocalDate.ofInstant(request.homeworkDueDate().toInstant(), ZoneId.systemDefault()));

            homeworkRepository.save(homework);
        }
    }

    @Override
    public boolean isSigned(Integer signedId, LocalDate date) {
        return false;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT') || hasRole('PARENT')")
    public List<TeachingTimeslotResponse> getWeekTimeslot(Integer classID, LocalDate date) {
        if (!schoolUserService.isLoggedParentStudentClass(classID)) {
            throw new AccessDeniedException("You are not allowed to access this class");
        } else if (authenticationService.isStudent()) {
            if (studentRepository.existsStudentByIdAndSchoolClasses_Id(authenticationService.getStudent().orElseThrow().getId(), classID)) {
                throw new AccessDeniedException("You are not allowed to access this class");
            }
        }

        var weekStart = DayOfWeek.MONDAY;
        var week = date.with(TemporalAdjusters.previousOrSame(weekStart));
        return timeslotMapper.toTeachingTimeslotResponseList(teachingTimeslotRepository.findTeachingTimeslotByClassTimetableSchoolClass_IdAndDateBetween(classID, week, week.plusDays(6)));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(Integer classId, LocalDate localDate) {
        if (teacherClassRepository.findByTeacher(authenticationService.getTeacher().orElseThrow()).stream()
                .noneMatch(c -> c.getSchoolClass().getId().equals(classId))) {
            throw new AccessDeniedException("You are not allowed to see this timetable");
        }

        var classTimetable = classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null);
        var teachingTimeslotList = teachingTimeslotRepository.findTeachingTimeslotByClassTimetableAndDate(classTimetable, localDate);
        return classAgendaMapper.toTeachingTimeslotDetailedResponseList(teachingTimeslotList, classActivityRepository, homeworkRepository);
    }

}
