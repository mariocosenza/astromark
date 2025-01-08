package it.astromark.communication.service;

import it.astromark.commons.service.CrudService;
import it.astromark.communication.dto.CommunicationRequest;
import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.entity.Communication;

import java.util.List;

public interface CommunicationService extends CrudService<Communication, CommunicationRequest, CommunicationResponse, Integer> {
    List<CommunicationResponse> getCommunicationBySchoolClassId(Integer schoolClassId);
}
