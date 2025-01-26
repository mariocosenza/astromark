package it.astromark.user.secretary.repository;

import it.astromark.user.secretary.entity.Secretary;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, UUID> {

    Secretary findByUsernameAndSchoolCode(String username, String schoolCode);

    Integer countByNameAndSurname(@Size(max = 64) @NotNull @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid name format") String name, @Size(max = 64) @NotNull @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid surname format") String surname);


}
