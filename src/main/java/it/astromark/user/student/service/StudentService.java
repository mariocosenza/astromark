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
    List<Integer> getStudentYears(@NotNull UUID studentId);
    List<SchoolClassResponse> getSchoolClassByYear(@NotNull UUID studentId, @PastOrPresent Year year);
    SchoolUserDetailed getById(@NotNull UUID studentId);
    SchoolUserDetailed create(@NotNull StudentRequest studentRequest);
    String attitude(@NotNull UUID studentId);
}
