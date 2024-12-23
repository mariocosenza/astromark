package it.astromark.classmanagement.didactic.entity;

import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teaching", schema = "astromark")
public class Teaching {
    @SequenceGenerator(name = "teaching_id_gen", sequenceName = "mark_id_seq", allocationSize = 1)
    @EmbeddedId
    private TeachingId id;

    @MapsId("teacherId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @MapsId("subjectTitle")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "subject_title", nullable = false)
    private Subject subjectTitle;

    @Size(max = 64)
    @NotNull
    @NotBlank
    @Column(name = "type_of_activity", nullable = false, length = 64)
    private String typeOfActivity;

}