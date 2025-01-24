package it.astromark.rating.serivice;

import it.astromark.rating.dto.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface MarkService {
    List<MarkResponse> getMarkByYear(UUID studentId, Year year);

    Double getAverage(UUID studentId, Year year);

    SemesterReportResponse getReport(@NotNull UUID studentId, @PositiveOrZero Short year, Boolean semester);

    SemesterReportResponse viewReport(Integer reportId);

    List<RatingsResponse> getRatings(Integer classId, LocalDate date);

    List<RatingsResponse> getEveryRatings(Integer classId);

    MarkResponse create(MarkRequest mark);

    MarkResponse update(MarkUpdateRequest mark, UUID studentId);

    boolean delete(Integer id);
}
