package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.commons.mapper.TimeslotMapper;
import it.astromark.agenda.schoolclass.dto.*;
import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.agenda.schoolclass.mapper.ClassAgendaHelperMapper;
import it.astromark.agenda.schoolclass.mapper.ClassAgendaMapper;
import it.astromark.agenda.schoolclass.repository.ClassTimetableRepository;
import it.astromark.agenda.schoolclass.repository.SignedHourRepository;
import it.astromark.agenda.schoolclass.repository.TeachingTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classwork.service.ClassworkService;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
public class ClassAgendaServiceImpl implements ClassAgendaService {

    private final TeachingTimeslotRepository teachingTimeslotRepository;
    private final TimeslotMapper timeslotMapper;
    private final SchoolUserService schoolUserService;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final ClassAgendaMapper classAgendaMapper;
    private final ClassAgendaHelperMapper classAgendaHelperMapper;
    private final SignedHourRepository signedHourRepository;
    private final ClassTimetableRepository classTimetableRepository;
    private final TeachingRepository teachingRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final ClassworkService classworkService;

    @Autowired
    public ClassAgendaServiceImpl(TeachingTimeslotRepository teachingTimeslotRepository, TimeslotMapper timeslotMapper, SchoolUserService schoolUserService, AuthenticationService authenticationService, StudentRepository studentRepository, ClassAgendaMapper classAgendaMapper, ClassAgendaHelperMapper classAgendaHelperMapper, SignedHourRepository signedHourRepository, ClassTimetableRepository classTimetableRepository, TeachingRepository teachingRepository, SchoolClassRepository schoolClassRepository, ClassworkService classworkService) {
        this.teachingTimeslotRepository = teachingTimeslotRepository;
        this.timeslotMapper = timeslotMapper;
        this.schoolUserService = schoolUserService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.classAgendaMapper = classAgendaMapper;
        this.classAgendaHelperMapper = classAgendaHelperMapper;
        this.signedHourRepository = signedHourRepository;
        this.classTimetableRepository = classTimetableRepository;
        this.teachingRepository = teachingRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.classworkService = classworkService;
    }


    @Override
    public Integer getTotalHour(Integer classId) {
        return 0;
    }

    @Override
    @Transactional
    public void addTimeslot(Integer classId, TeachingTimeslotRequest request) {
        var timeTable = classTimetableRepository.findById(request.timetableId()).orElseThrow();

        var hour = request.hour();

        var teaching = teachingRepository.findAll().stream()
                .filter(t -> t.getSubjectTitle().getTitle().equals(request.subject())
                        && t.getTeacher().getUsername().equals(request.username()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Teaching not found for subject: " + request.subject() + " and teacher username: " + request.username()));

        var startDate = timeTable.getStartValidity().plusDays(request.dayWeek() - 1);
        LocalDate endDate = timeTable.getEndValidity() != null ? timeTable.getEndValidity() : timeTable.getStartValidity().plusDays(270);
        var redDates = timeTable.getRedDates();

        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            var finalStartDate = startDate;
            if (redDates.stream().noneMatch(r -> r.getId().getDate().isEqual(finalStartDate))) {

                var existingTimeslot = teachingTimeslotRepository.findByClassTimetableAndDateAndHour(timeTable, finalStartDate, hour);

                if (existingTimeslot.isPresent()) {

                    var timeslotToUpdate = existingTimeslot.get();
                    timeslotToUpdate.setTeaching(teaching);
                    teachingTimeslotRepository.save(timeslotToUpdate);
                } else {

                    var teachingTimeslot = TeachingTimeslot.builder()
                            .classTimetable(timeTable)
                            .hour(hour)
                            .date(finalStartDate)
                            .teaching(teaching)
                            .build();

                    teachingTimeslotRepository.save(teachingTimeslot);
                }
            }
            startDate = startDate.plusDays(7);
        }
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public TeachingTimeslotDetailedResponse sign(Integer classId, SignHourRequest request) {
        if (!schoolUserService.isLoggedTeacherClass(classId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var teacher = authenticationService.getTeacher().orElseThrow();
        SignedHour signedHour = null;
        TeachingTimeslot slot;

        if (request.id() == null) {
            slot = TeachingTimeslot.builder()
                    .classTimetable(classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null))
                    .hour(request.hour().shortValue())
                    .date(request.date())
                    .teaching(teachingRepository.getReferenceById(
                            TeachingId.builder()
                                    .teacherId(teacher.getId())
                                    .subjectTitle(request.subject())
                                    .build()))
                    .build();

            teachingTimeslotRepository.save(slot);
        } else {
            slot = teachingTimeslotRepository.findById(request.id()).orElseThrow();
            if (!slot.getTeaching().getTeacher().getId().equals(teacher.getId())) {
                throw new AccessDeniedException("You are not allowed to sign this hour");
            }

            signedHour = signedHourRepository.findById(request.id()).orElse(null);
        }

        if (signedHour == null) {
            signedHour = SignedHour.builder()
                    .teachingTimeslot(slot)
                    .teacher(teacher)
                    .timeSign(Instant.now())
                    .build();

            signedHourRepository.save(signedHour);
        }

        if (request.activity() != null)
            classworkService.createActivity(request.activity(), signedHour);

        if (request.homework() != null)
            classworkService.createHomework(request.homework(), signedHour);

        return classAgendaMapper.toTeachingTimeslotDetailedResponse(slot, classAgendaHelperMapper);
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
            if (!studentRepository.existsStudentByIdAndSchoolClasses_Id(authenticationService.getStudent().orElseThrow().getId(), classID)) {
                throw new AccessDeniedException("You are not allowed to access this class");
            }
        }

        var weekStart = DayOfWeek.MONDAY;
        var week = date.with(TemporalAdjusters.previousOrSame(weekStart));
        return timeslotMapper.toTeachingTimeslotResponseList(teachingTimeslotRepository.findByClassTimetable_SchoolClass_IdAndDateBetween(classID, week, week.plusDays(6)));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public List<TeachingTimeslotDetailedResponse> getTeachingTimeslot(Integer classId, LocalDate localDate) {
        if (!schoolUserService.isLoggedTeacherClass(classId)) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var classTimetable = classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null);
        var teachingTimeslotList = teachingTimeslotRepository.findTeachingTimeslotByClassTimetableAndDate(classTimetable, localDate);
        return classAgendaMapper.toTeachingTimeslotDetailedResponseList(teachingTimeslotList, classAgendaHelperMapper);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SECRETARY')")
    public void createTimeTable(ClassTimeTableRequest request) {
        Integer schoolClassId = request.schoolClassId();

        var schoolClass = schoolClassRepository.findById(schoolClassId).orElseThrow(
                () -> new IllegalArgumentException("SchoolClass not found for ID: " + schoolClassId));

        if (request.endDate() != null && request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        assert request.endDate() != null;
        var endDate = request.endDate().isBefore(request.startDate())
                ? request.startDate().plusDays(1)
                : request.endDate();


        var classTimetable = ClassTimetable.builder()
                .schoolClass(schoolClass)
                .startValidity(request.startDate())
                .endValidity(endDate)
                .expectedHours(request.expectedHours() != null ? request.expectedHours() : 27)
                .build();

        classTimetableRepository.save(classTimetable);
    }

    @Override
    public List<TeachingTimeslotResponse> getClassTimeslot(Integer classId, LocalDate now) {

        var classTimeTable = classTimetableRepository
                .getClassTimetableBySchoolClass_IdAndEndValidityAfter(classId, now)
                .orElseGet(() -> classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidityIsNull(classId)
                        .orElse(null));

        if (classTimeTable == null) {
            return List.of();
        }

        var list = teachingTimeslotRepository.findByClassTimetableId(classTimeTable.getId())
                .stream()
                .sorted(Comparator.comparing(TeachingTimeslot::getDate))
                .filter(timeslot -> !timeslot.getDate().isBefore(now))
                .toList();

        return list.stream()
                .map(timeslot -> new TeachingTimeslotResponse(
                        timeslot.getHour(),
                        timeslot.getDate(),
                        timeslot.getTeaching().getSubjectTitle().getTitle()
                ))
                .toList();
    }


    @Override
    public List<TimeTableResponse> getTimeTable(Integer classId) {
        return classTimetableRepository.getClassTimetableBySchoolClass_Id(classId).stream()
                .map(timetable -> {
                    var schoolClass = schoolClassRepository.findById(timetable.getSchoolClass().getId())
                            .orElseThrow(() -> new IllegalArgumentException("SchoolClass not found for ID: " + timetable.getSchoolClass().getId()));

                    return new TimeTableResponse(
                            timetable.getId(),
                            timetable.getStartValidity(),
                            timetable.getEndValidity(),
                            schoolClass.getNumber(),
                            schoolClass.getLetter(),
                            schoolClass.getYear()
                    );
                })
                .toList();
    }


}
