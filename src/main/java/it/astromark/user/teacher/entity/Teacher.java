package it.astromark.user.teacher.entity;

import it.astromark.agenda.reception.entity.ReceptionTimetable;
import it.astromark.chat.entity.Ticket;
import it.astromark.classmanagement.entity.TeacherClass;
import it.astromark.classmanagement.entity.didactic.Teaching;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "teacher", schema = "astromark")
public class Teacher extends SchoolUser {

    @OneToMany(mappedBy = "teacher")
    private Set<ReceptionTimetable> receptionTimetables = new LinkedHashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherClass> teacherClasses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Teaching> teachings = new LinkedHashSet<>();
    @OneToMany(mappedBy = "teacher")
    private Set<Ticket> tickets = new LinkedHashSet<>();

}