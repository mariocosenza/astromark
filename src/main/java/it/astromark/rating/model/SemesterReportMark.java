package it.astromark.rating.model;

import it.astromark.classmanagement.entity.didactic.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "semester_report_mark", schema = "astromark")
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
    @Column(name = "mark", nullable = false)
    private Short mark;

}