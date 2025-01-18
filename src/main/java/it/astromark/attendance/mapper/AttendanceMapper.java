package it.astromark.attendance.mapper;

import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.repository.AbsenceRepository;
import it.astromark.attendance.repository.DelayRepository;
import it.astromark.user.student.entity.Student;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    @Mapping(target = "isAbsent", source = "student", qualifiedByName = "isAbsent")
    @Mapping(target = "isDelayed", source = "student", qualifiedByName = "isDelayed")
    @Mapping(target = "delayTime", source = "student", qualifiedByName = "getDelayTime")
    @Mapping(target = "totalAbsence", expression = "java(student.getAbsences().size())")
    @Mapping(target = "totalDelay", expression = "java(student.getDelays().size())")
    AttendanceResponse toAttendanceResponse(Student student, @Context LocalDate date, @Context AbsenceRepository absenceRepository, @Context DelayRepository delayRepository);

    List<AttendanceResponse> toAttendanceResponseList(List<Student> students, @Context LocalDate date, @Context AbsenceRepository absenceRepository, @Context DelayRepository delayRepository);

    @Named("isAbsent")
    default Boolean isAbsent(Student student, @Context LocalDate date, @Context AbsenceRepository absenceRepository) {
        return absenceRepository.findAbsenceByStudentAndDate(student, date) != null;
    }

    @Named("isDelayed")
    default Boolean isDelayed(Student student, @Context LocalDate date, @Context DelayRepository delayRepository) {
        var dateStart = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        var dateEnd = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return !delayRepository.findDelayByDateBetweenAndStudent_IdOrderByDateDesc(dateStart, dateEnd, student.getId()).isEmpty();
    }

    @Named("getDelayTime")
    default Instant getDelayTime(Student student, @Context LocalDate date, @Context DelayRepository delayRepository) {
        var dateStart = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        var dateEnd = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        var delay = delayRepository.findDelayByDateBetweenAndStudent_IdOrderByDateDesc(dateStart, dateEnd, student.getId());
        return delay.isEmpty() ? null : delay.getFirst().getDate();
    }
}

