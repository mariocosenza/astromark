package it.astromark.user.student.entity;

import it.astromark.attendance.entity.Absence;
import it.astromark.attendance.entity.Delay;
import it.astromark.behavior.entity.Note;
import it.astromark.chat.entity.HomeworkChat;
import it.astromark.chat.entity.Message;
import it.astromark.rating.model.Mark;
import it.astromark.rating.model.SemesterReport;
import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "student", schema = "astromark")
public class Student extends SchoolUser {

    @Column(name = "attitude", length = Integer.MAX_VALUE)
    private String attitude;

    @Column(name = "graduation_mark")
    private Short graduationMark;

    @Column(name = "latest_school_class")
    private Long latestSchoolClass;

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
    private Set<Message> messages = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Note> notes = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<SemesterReport> semesterReports = new LinkedHashSet<>();

}