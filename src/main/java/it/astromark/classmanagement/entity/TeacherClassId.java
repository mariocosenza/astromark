package it.astromark.classmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TeacherClassId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3055464861772122424L;
    @NotNull
    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;

    @NotNull
    @ColumnDefault("nextval('astromark.teacher_class_school_class_id_seq')")
    @Column(name = "school_class_id", nullable = false)
    private Integer schoolClassId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeacherClassId entity = (TeacherClassId) o;
        return Objects.equals(this.teacherId, entity.teacherId) &&
                Objects.equals(this.schoolClassId, entity.schoolClassId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, schoolClassId);
    }

}