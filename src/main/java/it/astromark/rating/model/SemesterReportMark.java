package it.astromark.rating.model;

import it.astromark.classmanagement.didactic.entity.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "semester_report_mark",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_semester_report_mark", columnNames = {"subject_title", "semester_id"})}
)
public class SemesterReportMark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_report_mark_id_gen")
    @SequenceGenerator(name = "semester_report_mark_id_gen", sequenceName = "semester_report_mark_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_title", nullable = false)
    private Subject subjectTitle;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.semester_report_mark_semester_id_seq')")
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterReport semester;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "mark", nullable = false)
    private Short mark;

}