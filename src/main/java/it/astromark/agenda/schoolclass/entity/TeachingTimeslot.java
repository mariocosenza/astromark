package it.astromark.agenda.schoolclass.entity;

import it.astromark.agenda.commons.entity.Timeslot;
import it.astromark.classmanagement.didactic.entity.Teaching;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "teaching_timeslot",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_teaching_timeslot", columnNames = {"class_timetable_id", "hour", "date"})}
)
public class TeachingTimeslot extends Timeslot {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "class_timetable_id", nullable = false)
    private ClassTimetable classTimetable;

    @OneToOne(mappedBy = "teachingTimeslot")
    private SignedHour signedHour;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumns({
            @JoinColumn(name = "teaching_teacher_id", referencedColumnName = "teacher_id", nullable = false),
            @JoinColumn(name = "teaching_subject_title", referencedColumnName = "subject_title", nullable = false)
    })
    private Teaching teaching;

}