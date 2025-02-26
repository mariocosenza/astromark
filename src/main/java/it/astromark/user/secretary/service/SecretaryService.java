package it.astromark.user.secretary.service;


import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.secretary.dto.SecretaryRequest;
import it.astromark.user.secretary.entity.Secretary;

import java.util.UUID;

/**
 * Service interface for managing secretary-related operations.
 * Extends the generic CrudService interface to provide basic CRUD functionality.
 */
public interface SecretaryService extends CrudService<Secretary, SecretaryRequest, SchoolUserDetailed, UUID> {
}

