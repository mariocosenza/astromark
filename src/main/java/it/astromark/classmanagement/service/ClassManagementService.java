package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;

import java.time.Year;
import java.util.List;

public interface ClassManagementService {

    /**
     * Retrieves the current school year.
     *
     * @return the current `Year` object
     * Pre-condition: None.
     * Post-condition: Returns the current school year as a `Year` object.
     */
    Year getYear();

    /**
     * Retrieves a list of all school classes.
     *
     * @return a list of `SchoolClassResponse` objects representing the school classes
     * Pre-condition: None.
     * Post-condition: Returns a list of all existing school classes.
     */
    List<SchoolClassResponse> getClasses();


}
