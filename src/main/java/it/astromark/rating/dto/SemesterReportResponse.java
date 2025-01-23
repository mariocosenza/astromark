package it.astromark.rating.dto;

import java.util.List;

public record SemesterReportResponse(Integer id, Boolean firstSemester, Boolean publicField, Boolean passed,
                                     Boolean viewed, Short year, List<SemesterReportMarkResponse> semesterReportMarks) {
}
