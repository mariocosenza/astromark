package it.astromark.rating.model;

import it.astromark.classmanagement.didactic.entity.Teaching;
import it.astromark.user.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@ToString(exclude = "student")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mark", schema = "astromark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mark_id_gen")
    @SequenceGenerator(name = "mark_id_gen", sequenceName = "mark_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumns({
            @JoinColumn(name = "teaching_teacher_id", referencedColumnName = "teacher_id", nullable = false),
            @JoinColumn(name = "teaching_subject_title", referencedColumnName = "subject_title", nullable = false),
    })
    private Teaching teaching;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "mark")
    private Double mark;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    @Column(name = "type", columnDefinition = "MarkType DEFAULT 'WRITTEN' NOT NULL")
    private MarkType type;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mark mark)) return false;
        return Objects.equals(id, mark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}