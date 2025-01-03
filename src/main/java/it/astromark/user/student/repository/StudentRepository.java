package it.astromark.user.student.repository;

import it.astromark.user.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Student findByUsernameAndSchoolCode(String username , String schoolCode);
}
