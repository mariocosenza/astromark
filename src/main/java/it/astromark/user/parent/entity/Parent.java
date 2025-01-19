package it.astromark.user.parent.entity;

import it.astromark.agenda.reception.entity.ReceptionBooking;
import it.astromark.chat.entity.Ticket;
import it.astromark.user.commons.model.SchoolUser;
import it.astromark.user.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "parent",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_parent_username_code_tax_id", columnNames = {"school_code", "tax_id"})}
)
public class Parent extends SchoolUser {

    @ColumnDefault("false")
    @NotNull
    @Column(name = "legal_guardian")
    private Boolean legalGuardian;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private Set<ReceptionBooking> receptionBookings = new LinkedHashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @Singular
    @JoinTable(name = "student_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private Set<Ticket> tickets = new LinkedHashSet<>();


}