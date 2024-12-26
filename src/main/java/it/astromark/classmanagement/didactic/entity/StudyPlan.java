package it.astromark.classmanagement.didactic.entity;

import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
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
    @OneToOne(fetch = FetchType.LAZY, optional = false)
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
    @JoinTable(name = "study_plan_subject",
            joinColumns = @JoinColumn(name = "study_plan_school_class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_title"))
    private Set<Subject> subjects = new LinkedHashSet<>();

}