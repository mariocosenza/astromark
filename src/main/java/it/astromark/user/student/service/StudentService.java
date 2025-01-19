package it.astromark.user.student.service;

import it.astromark.classmanagement.dto.SchoolClassResponse;
import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.student.dto.StudentRequest;
import it.astromark.user.student.entity.Student;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface StudentService extends CrudService<Student, StudentRequest, SchoolUserDetailed, UUID> {
    List<Integer> getStudentYears(UUID studentId);
    List<SchoolClassResponse>  getSchoolClassByYear(UUID studentId, Year year);
}
