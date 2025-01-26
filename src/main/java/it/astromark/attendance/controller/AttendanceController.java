package it.astromark.attendance.controller;

import it.astromark.attendance.dto.AttendanceRequest;
import it.astromark.attendance.dto.AttendanceResponse;
import it.astromark.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Retrieve attendance records",
            description = "Gets attendance records for a specified class and date."
    )
    @GetMapping("{classId}/attendance/{date}")
    public List<AttendanceResponse> getAttendance(@PathVariable Integer classId, @PathVariable LocalDate date) {
        return attendanceService.getAttendance(classId, date);
    }

    @Operation(
            summary = "Save attendance records",
            description = "Saves attendance records for a specified class and date."
    )
    @PostMapping("{classId}/attendance/{date}")
    public void saveAttendance(@PathVariable Integer classId, @PathVariable LocalDate date, @RequestBody List<AttendanceRequest> attendanceRequests) {
        attendanceService.saveAttendance(classId, date, attendanceRequests);
    }

}
