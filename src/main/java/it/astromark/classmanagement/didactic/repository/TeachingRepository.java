package it.astromark.classmanagement.didactic.repository;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.didactic.entity.TeachingId;
import it.astromark.user.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachingRepository extends JpaRepository<Teaching, TeachingId> {
    List<Teaching> findByTeacher(Teacher teacher);
}
