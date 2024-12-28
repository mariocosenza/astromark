package it.astromark.user.teacher.repository;

import it.astromark.school.entity.School;
import it.astromark.user.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    Teacher findByUsernameAndSchoolCode(String username , School schoolCode);
}
