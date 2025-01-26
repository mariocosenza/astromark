package it.astromark.classmanagement.service;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.dto.*;

import java.time.Year;
import java.util.List;
import java.util.UUID;

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

    List<SchoolClassStudentResponse> getStudents(Integer classId);

    List<TeachingResponse> getTeachings();


    void addTeaching(UUID teacheruuid, TeachingRequest teaching);

    List<String> getSubject();

    List<SchoolClassResponse> getTeacherClasses(UUID teacheruuid);

    void addTeacherToClass(UUID uuid, AddToClassRequest addToClassRequest);
}
