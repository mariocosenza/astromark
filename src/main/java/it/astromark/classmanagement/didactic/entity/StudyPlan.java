package it.astromark.classmanagement.didactic.entity;

import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "schoolClass")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_plan", schema = "astromark")
public class StudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "study_plan_id_gen")
    @SequenceGenerator(name = "study_plan_id_gen", sequenceName = "study_plan_school_class_id_seq", allocationSize = 1)
    @Column(name = "school_class_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ColumnDefault("nextval('astromark.study_plan_school_class_id_seq')")
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @Size(max = 64)
    @NotBlank
    @NotNull
    @Column(name = "title", length = 64, nullable = false)
    private String title;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "study_plan_subject",
            joinColumns = @JoinColumn(name = "study_plan_school_class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_title"))
    private Set<Subject> subjects = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StudyPlan studyPlan)) return false;
        return Objects.equals(id, studyPlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}