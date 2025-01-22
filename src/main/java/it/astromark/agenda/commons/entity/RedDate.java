package it.astromark.agenda.commons.entity;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "red_date", schema = "astromark")
public class RedDate {
    @EmbeddedId
    private RedDateId id;

    @MapsId("classTimetableId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_timetable_id", nullable = false)
    private ClassTimetable classTimetable;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RedDate redDate)) return false;

        return Objects.equals(id, redDate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RedDate{" +
                "id=" + id +
                '}';
    }
}