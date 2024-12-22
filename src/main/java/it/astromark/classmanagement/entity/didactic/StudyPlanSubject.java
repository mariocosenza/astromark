package it.astromark.classmanagement.entity.didactic;

import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "study_plan_subject", schema = "astromark")
public class StudyPlanSubject {
    @SequenceGenerator(name = "study_plan_subject_id_gen", sequenceName = "study_plan_school_class_id_seq", allocationSize = 1)
    @EmbeddedId
    private StudyPlanSubjectId id;

    @MapsId("subjectTitle")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_title", nullable = false)
    private Subject subjectTitle;

    @MapsId("studyPlanSchoolClassId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ColumnDefault("nextval('astromark.study_plan_subject_study_plan_school_class_id_seq')")
    @JoinColumn(name = "study_plan_school_class_id", nullable = false)
    private SchoolClass studyPlanSchoolClass;

}