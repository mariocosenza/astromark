package it.astromark.classmanagement.repository;

import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassRepository extends JpaRepository<TeacherClass, TeacherClassId> {
}
