package it.astromark.attendance.controller;

import it.astromark.attendance.dto.AttendanceRequest;
import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/classes/")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("{classId}/attendance/{date}")
    List<AttendanceResponse> getAttendance(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return attendanceService.getAttendance(classId, date);
    }

    @PostMapping("{classId}/attendance/{date}")
    void saveAttendance(@PathVariable Integer classId, @PathVariable LocalDate date, @RequestBody List<AttendanceRequest> attendanceRequests) {
        attendanceService.saveAttendance(classId, date, attendanceRequests);
    }

}
