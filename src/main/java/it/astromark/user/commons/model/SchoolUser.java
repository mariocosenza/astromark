package it.astromark.user.commons.model;

import it.astromark.school.entity.School;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Data
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_code", nullable = false)
    private School schoolCode;

    @Size(max = 256)
    @NotNull
    @Column(name = "username", nullable = false, length = 256)
    private String username;

    @Size(max = 256)
    @NotNull
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Size(max = 512)
    @NotNull
    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Size(max = 64)
    @NotNull
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Size(max = 64)
    @NotNull
    @Column(name = "surname", nullable = false, length = 64)
    private String surname;


    @Column(name = "tax_id", length = 16)
    private String taxId;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ColumnDefault("false")
    @Column(name = "gender")
    private Boolean gender;

    @Size(max = 512)
    @NotNull
    @Column(name = "residential_address", nullable = false, length = 512)
    private String residentialAddress;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    @Column(name = "PENDING_STATE", columnDefinition = "PendingState DEFAULT 'FIRST_LOGIN'")
    private PendingState pendingState;

}
