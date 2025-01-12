package it.astromark.user.student.repository;

import it.astromark.user.student.entity.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Student findByUsernameAndSchoolCode(String username , String schoolCode);
    boolean existsStudentByIdAndSchoolClasses_Id(UUID studentId, Integer classId);
    Integer countByNameAndSurname(@Size(max = 64) @NotNull @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid name format") String name, @Size(max = 64) @NotNull @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid surname format") String surname);

}
