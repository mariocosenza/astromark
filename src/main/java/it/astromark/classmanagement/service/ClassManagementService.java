package it.astromark.classmanagement.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;

import java.time.Year;
import java.util.List;

public interface ClassManagementService {

    Year getYear();

    List<SchoolClassResponse> getClasses();

}
