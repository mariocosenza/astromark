package it.astromark.agenda.schoolclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.astromark.classwork.dto.ClassActivityResponse;
import it.astromark.classwork.dto.HomeworkBriefResponse;
import it.astromark.user.commons.dto.SchoolUserResponse;

@Schema(description = "Response object containing detailed information about a teaching timeslot")
public record TeachingTimeslotDetailedResponse(
        Integer id,
        Integer hour,
        String subject,
        Boolean signed,
        SchoolUserResponse teacher,
        ClassActivityResponse activity,
        HomeworkBriefResponse homework
) {
}
