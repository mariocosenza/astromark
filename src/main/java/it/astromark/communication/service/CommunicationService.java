package it.astromark.communication.service;

import it.astromark.commons.service.CrudService;
import it.astromark.communication.dto.CommunicationRequest;
import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.entity.Communication;

import java.util.List;

/**
 * Service interface for managing communication-related operations.
 * Extends the generic CrudService interface to provide CRUD functionality for communications.
 */
public interface CommunicationService extends CrudService<Communication, CommunicationRequest, CommunicationResponse, Integer> {
    /**
     * Retrieves a list of communications for a specific school class.
     *
     * @param schoolClassId the ID of the school class
     * @return a list of `CommunicationResponse` objects representing the communications for the specified class
     * Pre-condition: The `schoolClassId` must not be null and must refer to an existing school class.
     * Post-condition: Returns a list of communications associated with the specified school class.
     */
    List<CommunicationResponse> getCommunicationBySchoolClassId(Integer schoolClassId);

}
