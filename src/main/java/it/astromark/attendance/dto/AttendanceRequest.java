package it.astromark.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Request object for submitting attendance details for a student")
public record AttendanceRequest(
        UUID studentId,
        Boolean isAbsent,
        Boolean isDelayed,
        Integer delayTimeHour,
        Integer delayTimeMinute,
        Boolean delayNeedJustification
) {
}
