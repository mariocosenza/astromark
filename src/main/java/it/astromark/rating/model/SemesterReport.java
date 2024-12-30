package it.astromark.rating.model;

import it.astromark.user.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "semester_report",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_semester_report", columnNames = {"first_semester", "year", "student_id"})}
)
public class SemesterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_report_id_gen")
    @SequenceGenerator(name = "semester_report_id_gen", sequenceName = "semester_report_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Builder.Default
    @ColumnDefault("true")
    @Column(name = "first_semester", nullable = false)
    private Boolean firstSemester = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "public", nullable = false)
    private Boolean publicField = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "passed", nullable = false)
    private Boolean passed = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "viewed", nullable = false)
    private Boolean viewed = false;

    @PositiveOrZero
    @Column(name = "year")
    private Short year;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id")
    private Student student;

    @Builder.Default
    @OneToMany(mappedBy = "semester")
    private Set<SemesterReportMark> semesterReportMarks = new LinkedHashSet<>();

}