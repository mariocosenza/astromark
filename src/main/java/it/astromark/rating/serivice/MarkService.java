package it.astromark.rating.serivice;

import it.astromark.rating.dto.MarkRequest;
import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.dto.MarkUpdateRequest;
import it.astromark.rating.dto.SemesterReportResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface MarkService {
    List<MarkResponse> getMarkByYear(UUID studentId, Year year);

    Double getAverage(UUID studentId, Year year);

    SemesterReportResponse getReport(@NotNull UUID studentId, @PositiveOrZero Short year, Boolean semester);

    SemesterReportResponse viewReport(Integer reportId);

    MarkResponse create(MarkRequest mark);

    MarkResponse update(MarkUpdateRequest mark, UUID studentId);

    boolean delete(Integer id);
}
