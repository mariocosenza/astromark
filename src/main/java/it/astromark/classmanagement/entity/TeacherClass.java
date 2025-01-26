package it.astromark.classmanagement.entity;

import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher_class", schema = "astromark")
public class TeacherClass {
    @EmbeddedId
    private TeacherClassId id;

    @MapsId("teacherId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @MapsId("schoolClassId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @ColumnDefault("nextval('astromark.teacher_class_school_class_id_seq')")
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "coordinator", nullable = false)
    private Boolean coordinator = false;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TeacherClass that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}