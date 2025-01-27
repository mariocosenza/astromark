package it.astromark.user.commons.model;

import it.astromark.school.entity.School;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

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

    @Builder.Default
    @ColumnDefault("false")
    @NotNull
    @Column(name = "gender")
    private Boolean male = false;

    @Size(max = 512)
    @NotBlank
    @Column(name = "residential_address", nullable = false, length = 512)
    private String residentialAddress;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    @Column(name = "PENDING_STATE", columnDefinition = "PendingState DEFAULT 'FIRST_LOGIN'")
    private PendingState pendingState;


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
