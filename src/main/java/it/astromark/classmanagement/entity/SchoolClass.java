package it.astromark.classmanagement.entity;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import it.astromark.classmanagement.entity.didactic.StudyPlan;
import it.astromark.communication.entity.Communication;
import it.astromark.school.entity.School;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_class", schema = "astromark")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_class_id_gen")
    @SequenceGenerator(name = "school_class_id_gen", sequenceName = "school_class_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "school_code", nullable = false)
    private School schoolCode;

    @NotNull
    @Column(name = "number", nullable = false)
    private Short number;

    @Size(max = 2)
    @NotNull
    @Column(name = "letter", nullable = false, length = 2)
    private String letter;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;


    @Builder.Default
    @OneToMany(mappedBy = "schoolClass")
    private Set<ClassTimetable> classTimetables = new LinkedHashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "schoolClass")
    private Set<Communication> communications = new LinkedHashSet<>();

    @OneToOne(mappedBy = "schoolClass")
    private StudyPlan studyPlan;

    @Builder.Default
    @OneToMany(mappedBy = "schoolClass")
    private Set<TeacherClass> teacherClasses = new LinkedHashSet<>();

}