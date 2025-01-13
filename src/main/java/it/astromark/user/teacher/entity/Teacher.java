package it.astromark.user.teacher.entity;

import it.astromark.agenda.reception.entity.ReceptionTimetable;
import it.astromark.chat.entity.Ticket;
import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(
        name = "teacher",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_teacher_username_code_tax_id", columnNames = {"school_code", "tax_id"})}
)
public class Teacher extends SchoolUser {

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private Set<ReceptionTimetable> receptionTimetables = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private Set<TeacherClass> teacherClasses = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private Set<Teaching> teachings = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private Set<Ticket> tickets = new LinkedHashSet<>();

}