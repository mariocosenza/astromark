package it.astromark.agenda.schoolclass.entity;

import it.astromark.agenda.commons.entity.Timeslot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "teaching_timeslot", schema = "astromark")
public class TeachingTimeslot extends Timeslot {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "class_timetable_id", nullable = false)
    private ClassTimetable classTimetable;

    @OneToOne(mappedBy = "teachingTimeslot")
    private SignedHour signedHour;

}