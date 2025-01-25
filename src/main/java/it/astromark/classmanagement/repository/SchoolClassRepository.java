package it.astromark.classmanagement.repository;

import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Integer> {

    SchoolClass findByNumberAndLetter(@NotNull @PositiveOrZero Short number, @Size(max = 2) @NotNull @Pattern(regexp = "^[A-Z]{1,2}$", message = "One to two alphabet letter allowed") String letter);
}
