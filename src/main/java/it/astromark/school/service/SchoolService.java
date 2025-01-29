package it.astromark.school.service;

import it.astromark.commons.service.CrudService;
import it.astromark.school.dto.SchoolRequest;
import it.astromark.school.dto.SchoolResponse;
import it.astromark.school.entity.School;

/**
 * Service interface for managing school entities.
 * Extends the generic `CrudService` for standard CRUD operations with specific types.
 */
public interface SchoolService extends CrudService<School, SchoolRequest, SchoolResponse, String> {

}
