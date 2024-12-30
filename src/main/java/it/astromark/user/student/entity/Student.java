package it.astromark.user.student.entity;

import it.astromark.attendance.entity.Absence;
import it.astromark.attendance.entity.Delay;
import it.astromark.behavior.entity.Note;
import it.astromark.chat.entity.HomeworkChat;
import it.astromark.classmanagement.entity.SchoolClass;
import it.astromark.rating.model.Mark;
import it.astromark.rating.model.SemesterReport;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "student",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_student_username_code_tax_id", columnNames = {"username","school_code", "tax_id"})}
)
public class Student extends SchoolUser {

    @Column(name = "attitude", length = Integer.MAX_VALUE)
    private String attitude;

    @Max(100)
    @Min(60) //null value are considered valid
    @Column(name = "graduation_mark")
    private Short graduationMark;

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Absence> absences = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Delay> delays = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<HomeworkChat> homeworkChats = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Mark> marks = new LinkedHashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Note> notes = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<SemesterReport> semesterReports = new LinkedHashSet<>();

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "student_school_class",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "school_class_id"))
    private Set<SchoolClass> schoolClasses = new LinkedHashSet<>();

}