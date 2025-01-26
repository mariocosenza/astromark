package it.astromark.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response object containing details of a semester report")
public record SemesterReportResponse(
        Integer id,
        Boolean firstSemester,
        Boolean publicField,
        Boolean passed,
        Boolean viewed,
        Short year,
        List<SemesterReportMarkResponse> semesterReportMarks
) {
}
