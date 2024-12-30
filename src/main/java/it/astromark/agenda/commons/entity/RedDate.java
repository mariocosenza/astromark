package it.astromark.agenda.commons.entity;

import it.astromark.agenda.schoolclass.entity.ClassTimetable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
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

}