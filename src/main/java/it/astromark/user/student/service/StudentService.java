package it.astromark.user.student.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    /**
     * Retrieves a list of years associated with a specific student.
     *
     * @param studentId the UUID of the student
     * @return a list of integers representing the years the student has been enrolled
     * Pre-condition: The `studentId` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of years associated with the specified student.
     */
    List<Integer> getStudentYears(@NotNull UUID studentId);
    /**
     * Retrieves the school classes associated with a specific student for a given year.
     *
     * @param studentId the UUID of the student
     * @param year the year to filter school classes
     * @return a list of `SchoolClassResponse` objects representing the classes of the student for the specified year
     * Pre-condition: The `studentId` and `year` must not be null. The student associated with the `studentId` must exist.
     * Post-condition: Returns a list of school classes associated with the specified student and year.
     */
    List<SchoolClassResponse> getSchoolClassByYear(@NotNull UUID studentId, @PastOrPresent Year year);
    SchoolUserDetailed getById(@NotNull UUID studentId);
    SchoolUserDetailed create(@NotNull StudentRequest studentRequest);
    String attitude(@NotNull UUID studentId);
}
