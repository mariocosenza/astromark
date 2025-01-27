package it.astromark.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response object containing attendance details for a student")
public record AttendanceResponse(
        UUID id,
        String name,
        String surname,
        Boolean isAbsent,
        Boolean isDelayed,
        Instant delayTime,
        Boolean delayNeedJustification,
        Integer totalAbsence,
        Integer totalDelay
) {
}
