package it.astromark.attendance.dto;

import java.time.Instant;
import java.util.UUID;

public record AttendanceResponse(UUID id, String name, String surname, Boolean isAbsent, Boolean isDelayed, Instant delayTime, Integer totalAbsence, Integer totalDelay) {
}
