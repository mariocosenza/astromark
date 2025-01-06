package it.astromark.rating.serivice;

import it.astromark.rating.dto.MarkResponse;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface MarkService {
    List<MarkResponse> getMarkByYear(UUID studentId, Year year);

}
