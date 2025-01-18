package it.astromark.attendance.service;

import it.astromark.attendance.dto.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> getAttendance(Integer classId, LocalDate date);
}
