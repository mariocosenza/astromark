package it.astromark.school.entity;

import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school", schema = "astromark")
public class School {
    @Id
    @Size(max = 7)
    @Pattern(regexp = "^SS\\d{5}$")
    @SequenceGenerator(name = "school_id_gen", sequenceName = "school_class_id_seq", allocationSize = 1)
    @Column(name = "code", nullable = false, length = 7)
    private String code;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Size(max = 512)
    @NotNull
    @Column(name = "address", nullable = false, length = 512)
    private String address;

    @Size(max = 256)
    @Column(name = "name", length = 256)
    private String name;

    @Size(max = 256)
    @NotNull
    @Email
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Size(max = 129)
    @Column(name = "school_principal_full_name", length = 129)
    private String schoolPrincipalFullName;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private Set<Parent> parents = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private Set<SchoolClass> schoolClasses = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private Set<Secretary> secretaries = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private Set<Student> students = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private Set<Teacher> teachers = new LinkedHashSet<>();

    @Override
    public String toString() {
        return "School{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", code='" + code + '\'' +
                ", schoolPrincipalFullName='" + schoolPrincipalFullName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof School school)) return false;
        return Objects.equals(code, school.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}