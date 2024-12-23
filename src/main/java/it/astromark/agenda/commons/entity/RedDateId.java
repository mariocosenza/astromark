package it.astromark.agenda.commons.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RedDateId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5284321312411931669L;

    @NotNull
    @Column(name = "class_timetable_id", nullable = false)
    private Integer classTimetableId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RedDateId entity = (RedDateId) o;
        return Objects.equals(this.date, entity.date) &&
                Objects.equals(this.classTimetableId, entity.classTimetableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, classTimetableId);
    }

}