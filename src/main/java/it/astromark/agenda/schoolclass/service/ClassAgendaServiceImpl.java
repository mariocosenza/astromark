package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.commons.mapper.TimeslotMapper;
import it.astromark.agenda.schoolclass.dto.SignHourRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotDetailedResponse;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotRequest;
import it.astromark.agenda.schoolclass.dto.TeachingTimeslotResponse;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.agenda.schoolclass.mapper.ClassAgendaMapper;
import it.astromark.agenda.schoolclass.repository.TeachingTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
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
import java.time.LocalDate;
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

    @Autowired
    public ClassAgendaServiceImpl(TeachingTimeslotRepository teachingTimeslotRepository, TimeslotMapper timeslotMapper, SchoolUserService schoolUserService, AuthenticationService authenticationService, StudentRepository studentRepository, ClassAgendaMapper classAgendaMapper, ClassActivityRepository classActivityRepository, HomeworkRepository homeworkRepository) {
        this.teachingTimeslotRepository = teachingTimeslotRepository;
        this.timeslotMapper = timeslotMapper;
        this.schoolUserService = schoolUserService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.classAgendaMapper = classAgendaMapper;
        this.classActivityRepository = classActivityRepository;
        this.homeworkRepository = homeworkRepository;
    }


    @Override
    public Integer getTotalHour(Integer classId) {
        return 0;
    }

    @Override
    public void addTimeslot(Integer classId, TeachingTimeslotRequest request) {

    }

    @Override
    public void sign(Integer classId, SignHourRequest request) {

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
    public List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(Integer classId, LocalDate localDate) {
        List<TeachingTimeslot> teachingTimeslotList = teachingTimeslotRepository.findByClassTimetableSchoolClass_IdAndDate(classId, localDate);
        return classAgendaMapper.toTeachingTimeslotDetailedResponseList(teachingTimeslotList, classActivityRepository, homeworkRepository);
    }

    @Override
    public void createTimeTable(Integer classId, TeachingTimeslotRequest request) {

    }

}
