package it.astromark.user.parent.service;

import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.parent.dto.ParentDetailedResponse;
import it.astromark.user.parent.dto.ParentRequest;
import it.astromark.user.parent.entity.Parent;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing parent-related operations.
 * Extends the generic CrudService interface for basic CRUD operations.
 */

public interface ParentService extends CrudService<Parent, ParentRequest, ParentDetailedResponse, UUID> {

    /**
     * Retrieves a list of all students associated with a parent.
     *
     * @return a list of `SchoolUserDetailed` objects representing the students
     * Pre-condition: None.
     * Post-condition: Returns a list of detailed information about all students associated with the parent.
     */
    List<SchoolUserDetailed> getStudents();

    /**
     * Retrieves a list of all teachers.
     *
     * @return a list of `SchoolUserResponse` objects representing the teachers
     * Pre-condition: None.
     * Post-condition: Returns a list of information about all teachers.
     */
    List<SchoolUserResponse> getTeachers();
}

