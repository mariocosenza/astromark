package it.astromark.agenda.schoolclass.service;

import it.astromark.agenda.commons.mapper.TimeslotMapper;
import it.astromark.agenda.schoolclass.dto.*;
import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.agenda.schoolclass.entity.TeachingTimeslot;
import it.astromark.agenda.schoolclass.mapper.ClassAgendaMapper;
import it.astromark.agenda.schoolclass.repository.ClassTimetableRepository;
import it.astromark.agenda.schoolclass.repository.SignedHourRepository;
import it.astromark.agenda.schoolclass.repository.TeachingTimeslotRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.didactic.repository.StudyPlanRepository;
import it.astromark.classmanagement.didactic.repository.TeachingRepository;
import it.astromark.classmanagement.repository.SchoolClassRepository;
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
import java.util.Comparator;
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
    private final ClassTimetableRepository classTimetableRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final TeachingRepository teachingRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Autowired
    public ClassAgendaServiceImpl(TeachingTimeslotRepository teachingTimeslotRepository, TimeslotMapper timeslotMapper, SchoolUserService schoolUserService, AuthenticationService authenticationService, StudentRepository studentRepository, ClassAgendaMapper classAgendaMapper, ClassActivityRepository classActivityRepository, HomeworkRepository homeworkRepository, SignedHourRepository signedHourRepository, ClassTimetableRepository classTimetableRepository, StudyPlanRepository studyPlanRepository, TeachingRepository teachingRepository, SchoolClassRepository schoolClassRepository) {
        this.teachingTimeslotRepository = teachingTimeslotRepository;
        this.timeslotMapper = timeslotMapper;
        this.schoolUserService = schoolUserService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
        this.classAgendaMapper = classAgendaMapper;
        this.classActivityRepository = classActivityRepository;
        this.homeworkRepository = homeworkRepository;
        this.signedHourRepository = signedHourRepository;
        this.classTimetableRepository = classTimetableRepository;
        this.studyPlanRepository = studyPlanRepository;
        this.teachingRepository = teachingRepository;
        this.schoolClassRepository = schoolClassRepository;
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
                // Cerca se il timeslot esiste già
                var existingTimeslot = teachingTimeslotRepository.findByClassTimetableAndDateAndHour(timeTable, finalStartDate, hour);

                if (existingTimeslot.isPresent()) {
                    // Aggiorna il timeslot esistente
                    var timeslotToUpdate = existingTimeslot.get();
                    timeslotToUpdate.setTeaching(teaching);
                    teachingTimeslotRepository.save(timeslotToUpdate);
                } else {
                    // Crea un nuovo timeslot
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
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var teacher = authenticationService.getTeacher().orElseThrow();

        SignedHour signedHour = null;
        ClassActivity activity = null;
        Homework homework = null;
        TeachingTimeslot slot = null;

        if (request.slotId() == null) {
            slot = new TeachingTimeslot();
            slot.setClassTimetable(classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null));
            slot.setHour(request.hour().shortValue());
            slot.setDate(LocalDate.ofInstant(request.date().toInstant(), ZoneId.systemDefault()));

            var subjects = studyPlanRepository.findBySchoolClass_Id(classId).stream()
                    .flatMap(sp -> sp.getSubjects().stream()).toList();

            slot.setTeaching(teachingRepository.findByTeacher(teacher).stream()
                    .filter(c -> subjects.contains(c.getSubjectTitle()))
                    .findFirst().orElseThrow());

            teachingTimeslotRepository.save(slot);

        } else {
            signedHour = signedHourRepository.findById(request.slotId()).orElse(null);
        }

        if (signedHour != null) {
            if (!signedHour.getTeacher().getId().equals(teacher.getId())) {
                throw new AccessDeniedException("You are not allowed to edit this hour");
            }

            activity = classActivityRepository.findBySignedHour(signedHour);
            homework = homeworkRepository.findBySignedHour(signedHour);
        } else {
            if (request.slotId() != null) {
                slot = teachingTimeslotRepository.findById(request.slotId()).orElseThrow();
                if (!slot.getTeaching().getTeacher().getId().equals(teacher.getId())) {
                    throw new AccessDeniedException("You are not allowed to sign this hour");
                }
            }

            signedHour = new SignedHour();
            signedHour.setTeachingTimeslot(slot);
            signedHour.setTeacher(teacher);
            signedHour.setTimeSign(Instant.now());

            signedHourRepository.save(signedHour);
        }

        if (!request.activityTitle().isEmpty()) {
            if (activity == null) {
                activity = new ClassActivity();
                activity.setSignedHour(signedHour);
            }

            activity.setTitle(request.activityTitle());
            activity.setDescription(request.activityDescription());

            classActivityRepository.save(activity);
        }

        if (!request.homeworkTitle().isEmpty()) {
            if (homework == null) {
                homework = new Homework();
                homework.setSignedHour(signedHour);
            }

            homework.setTitle(request.homeworkTitle());
            homework.setDescription(request.homeworkDescription());
            homework.setDueDate(LocalDate.ofInstant(request.homeworkDueDate().toInstant(), ZoneId.systemDefault()));

            homeworkRepository.save(homework);
        }

        return classAgendaMapper.toTeachingTimeslotDetailedResponse(signedHour.getTeachingTimeslot(), classActivityRepository, homeworkRepository);
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
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var classTimetable = classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null);
        var teachingTimeslotList = teachingTimeslotRepository.findTeachingTimeslotByClassTimetableAndDate(classTimetable, localDate);
        return classAgendaMapper.toTeachingTimeslotDetailedResponseList(teachingTimeslotList, classActivityRepository, homeworkRepository);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SECRETARY')")
    public void createTimeTable(ClassTimeTableRequest request) {
        Integer schoolClassId = request.schoolClassId();

        var schoolClass = schoolClassRepository.findById(schoolClassId).orElseThrow(
                () -> new IllegalArgumentException("SchoolClass not found for ID: " + schoolClassId));


        var classTimetable = ClassTimetable.builder()
                .schoolClass(schoolClass)
                .startValidity(request.startDate())
                .endValidity(request.endDate())
                .expectedHours(request.expectedHours() != null ? request.expectedHours() : 27) // Valore predefinito
                .build();

        classTimetableRepository.save(classTimetable);
    }

    @Override
    public List<TeachingTimeslotResponse> getClassTimeslot(Integer classId, LocalDate now) {

        var classTimeTable = classTimetableRepository.getClassTimetableBySchoolClass_IdAndEndValidity(classId, null);

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
