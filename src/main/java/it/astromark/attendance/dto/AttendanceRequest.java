package it.astromark.attendance.dto;

import java.util.UUID;

public record AttendanceRequest(UUID studentId, Boolean isAbsent, Boolean isDelayed, Integer delayTimeHour, Integer delayTimeMinute, Boolean delayNeedJustification) {
}
