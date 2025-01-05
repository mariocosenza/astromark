package it.astromark.user.commons.model;

import it.astromark.school.entity.School;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class SchoolUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "school_code", nullable = false)
    private School school;

    @Size(max = 256)
    @NotBlank
    @Column(name = "username", nullable = false, length = 256)
    private String username;

    @Size(max = 256)
    @NotNull
    @Email
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Size(max = 512)
    @NotNull
    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Size(max = 64)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid name format")
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Size(max = 64)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$", message = "Invalid surname format")
    @Column(name = "surname", nullable = false, length = 64)
    private String surname;


    @Size(min = 16)
    @Size(max = 16)
    @Column(name = "tax_id", length = 16)
    private String taxId;

    @NotNull
    @Past(message = "Needs to be at least 10 year old")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ColumnDefault("false")
    @NotNull
    @Column(name = "gender")
    private Boolean gender;

    @Size(max = 512)
    @NotBlank
    @Column(name = "residential_address", nullable = false, length = 512)
    private String residentialAddress;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    @Column(name = "PENDING_STATE", columnDefinition = "PendingState DEFAULT 'FIRST_LOGIN'")
    private PendingState pendingState;

    public boolean isStudent() {
        return this instanceof Student;
    }

    public boolean isTeacher() {
        return this instanceof Teacher;
    }

    public boolean isParent() {
        return this instanceof Parent;
    }

    public boolean isSecretary() {
        return this instanceof Secretary;
    }

    public Optional<Parent> getParent() {
        return isParent() ? Optional.of((Parent) this) : Optional.empty();
    }

    public Optional<Student> getStudent() {
        return isStudent() ? Optional.of((Student) this) : Optional.empty();
    }

    public Optional<Teacher> getTeacher() {
        return isTeacher() ? Optional.of((Teacher) this) : Optional.empty();
    }

    public Optional<Secretary> getSecretary() {
        return isSecretary() ? Optional.of((Secretary) this) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SchoolUser that)) return false;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
