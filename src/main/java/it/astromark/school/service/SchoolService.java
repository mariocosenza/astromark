package it.astromark.school.service;

import it.astromark.commons.service.CrudService;
import it.astromark.school.dto.SchoolRequest;
import it.astromark.school.dto.SchoolResponse;
import it.astromark.school.entity.School;

public interface SchoolService extends CrudService<School, SchoolRequest, SchoolResponse, String> {
}
