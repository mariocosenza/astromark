package it.astromark.rating.serivice;

import it.astromark.commons.service.CrudService;
import it.astromark.rating.dto.MarkResponse;
import it.astromark.rating.model.Mark;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface MarkService extends CrudService<Mark, Mark, MarkResponse, Integer> {
    List<MarkResponse> getMarkByYear(UUID studentId, Year year);
    Double getAverage(UUID studentId, Year year);

}
