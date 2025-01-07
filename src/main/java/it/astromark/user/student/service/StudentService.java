package it.astromark.user.student.service;

import it.astromark.commons.service.CrudService;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.student.entity.Student;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface StudentService extends CrudService<Student, Student, Student, UUID> {
    List<Year> getStudentYears(UUID studentId);
}
