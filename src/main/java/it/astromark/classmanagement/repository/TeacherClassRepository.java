package it.astromark.classmanagement.repository;

import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.TeacherClassId;
import it.astromark.user.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassRepository extends JpaRepository<TeacherClass, TeacherClassId> {

    List<TeacherClass> findByTeacher(Teacher teacher);
}
