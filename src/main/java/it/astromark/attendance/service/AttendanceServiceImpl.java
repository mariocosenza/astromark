package it.astromark.attendance.service;

import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.mapper.AttendanceMapper;
import it.astromark.attendance.repository.AbsenceRepository;
import it.astromark.attendance.repository.DelayRepository;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.classmanagement.repository.SchoolClassRepository;
import it.astromark.classmanagement.repository.TeacherClassRepository;
import it.astromark.user.commons.model.SchoolUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final TeacherClassRepository teacherClassRepository;
    private final AuthenticationService authenticationService;
    private final SchoolClassRepository schoolClassRepository;
    private final AbsenceRepository absenceRepository;
    private final DelayRepository delayRepository;
    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(TeacherClassRepository teacherClassRepository, AuthenticationService authenticationService, SchoolClassRepository schoolClassRepository, AbsenceRepository absenceRepository, DelayRepository delayRepository, AttendanceMapper attendanceMapper) {
        this.teacherClassRepository = teacherClassRepository;
        this.authenticationService = authenticationService;
        this.schoolClassRepository = schoolClassRepository;
        this.absenceRepository = absenceRepository;
        this.delayRepository = delayRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('teacher')")
    public List<AttendanceResponse> getAttendance(Integer classId, LocalDate date) {
        if (teacherClassRepository.findByTeacher(authenticationService.getTeacher().orElseThrow()).stream()
                .noneMatch(c -> c.getSchoolClass().getId().equals(classId))) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        var students = schoolClassRepository.findById(classId).orElseThrow()
                .getStudents().stream()
                .sorted(Comparator.comparing(SchoolUser::getSurname))
                .toList();
        return attendanceMapper.toAttendanceResponseList(students, date, absenceRepository, delayRepository);
    }
}
