package it.astromark.classmanagement.entity.didactic;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StudyPlanSubjectId implements java.io.Serializable {
    private static final long serialVersionUID = 7884693267848906718L;
    @Size(max = 64)
    @NotNull
    @Column(name = "subject_title", nullable = false, length = 64)
    private String subjectTitle;

    @NotNull
    @ColumnDefault("nextval('astromark.study_plan_subject_study_plan_school_class_id_seq')")
    @Column(name = "study_plan_school_class_id", nullable = false)
    private Integer studyPlanSchoolClassId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudyPlanSubjectId entity = (StudyPlanSubjectId) o;
        return Objects.equals(this.studyPlanSchoolClassId, entity.studyPlanSchoolClassId) &&
                Objects.equals(this.subjectTitle, entity.subjectTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studyPlanSchoolClassId, subjectTitle);
    }

}