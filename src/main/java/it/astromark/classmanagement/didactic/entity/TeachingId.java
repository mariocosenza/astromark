package it.astromark.classmanagement.didactic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class TeachingId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7656541957426848058L;
    @NotNull
    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;

    @Size(max = 64)
    @NotNull
    @Column(name = "subject_title", nullable = false, length = 64)
    private String subjectTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeachingId entity = (TeachingId) o;
        return Objects.equals(this.teacherId, entity.teacherId) &&
                Objects.equals(this.subjectTitle, entity.subjectTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, subjectTitle);
    }

}