package it.astromark.user.parent.service;

import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.dto.ParentRequest;
import it.astromark.user.parent.entity.Parent;

import java.util.List;
import java.util.UUID;

public interface ParentService extends CrudService<Parent, ParentRequest, ParentDetailedResponse, UUID> {

    List<SchoolUserDetailed> getStudents();


}
