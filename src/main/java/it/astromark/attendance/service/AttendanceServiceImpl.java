package it.astromark.attendance.service;

import it.astromark.attendance.dto.AttendanceRequest;
import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.entity.Absence;
import it.astromark.attendance.entity.Delay;
import it.astromark.attendance.mapper.AttendanceMapper;
import it.astromark.attendance.repository.AbsenceRepository;
import it.astromark.attendance.repository.DelayRepository;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.commons.exception.GlobalExceptionHandler;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.commons.service.SchoolUserService;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final SchoolUserService schoolUserService;
    private final SchoolClassRepository schoolClassRepository;
    private final AbsenceRepository absenceRepository;
    private final DelayRepository delayRepository;
    private final AttendanceMapper attendanceMapper;
    private final StudentRepository studentRepository;
    private final JustifiableService justifiableService;

    @Autowired
    public AttendanceServiceImpl(SchoolUserService schoolUserService, SchoolClassRepository schoolClassRepository, AbsenceRepository absenceRepository, DelayRepository delayRepository, AttendanceMapper attendanceMapper, StudentRepository studentRepository, JustifiableService justifiableService) {
        this.schoolUserService = schoolUserService;
        this.schoolClassRepository = schoolClassRepository;
        this.absenceRepository = absenceRepository;
        this.delayRepository = delayRepository;
        this.attendanceMapper = attendanceMapper;
        this.studentRepository = studentRepository;
        this.justifiableService = justifiableService;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public List<AttendanceResponse> getAttendance(@NotNull Integer classId, @NotNull LocalDate date) {
        if (!schoolUserService.isLoggedTeacherClass(classId) || date.isAfter(LocalDate.now())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var students = schoolClassRepository.findById(classId).orElseThrow()
                .getStudents().stream()
                .sorted(Comparator.comparing(SchoolUser::getSurname))
                .toList();

        return attendanceMapper.toAttendanceResponseList(students, date, absenceRepository, delayRepository, justifiableService);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER')")
    public void saveAttendance(@NotNull Integer classId, @NotNull LocalDate date, @NotNull List<AttendanceRequest> attendanceRequests) {
        if (!schoolUserService.isLoggedTeacherClass(classId) || date.isAfter(LocalDate.now())) {
            throw new AccessDeniedException(GlobalExceptionHandler.AUTHORIZATION_DENIED);
        }

        var dateStart = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        var dateEnd = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        for (AttendanceRequest attendance : attendanceRequests) {
            var student = studentRepository.findById(attendance.studentId()).orElseThrow();

            if (student.getSchoolClasses().stream().noneMatch(c -> c.getId().equals(classId))) {
                throw new AccessDeniedException("This student it's not in this class");
            }

            var delays = delayRepository.findDelayByDateBetweenAndStudent_IdOrderByDateDesc(dateStart, dateEnd, student.getId());
            var absence = absenceRepository.findAbsenceByStudentAndDate(student, date);
            var delay = delays.stream().findFirst().orElse(null);

            if (attendance.isAbsent() && absence == null) {
                absence = Absence.builder()
                        .student(student)
                        .date(date)
                        .justified(false)
                        .needsJustification(true)
                        .build();
                absenceRepository.save(absence);
            } else if (absence != null) {
                absenceRepository.delete(absence);
            }


            if (attendance.isDelayed()) {
                if (delay == null) {
                    delay = new Delay();
                    delay.setStudent(student);
                }

                delay.setNeedsJustification(attendance.delayNeedJustification());
                delay.setJustified(!attendance.delayNeedJustification());
                delay.setDate(date.atStartOfDay()
                        .withHour(attendance.delayTimeHour())
                        .withMinute(attendance.delayTimeMinute())
                        .toInstant(ZoneOffset.UTC));
                delayRepository.save(delay);
            } else if (delay != null) {
                delayRepository.delete(delay);
            }
        }
    }
}
