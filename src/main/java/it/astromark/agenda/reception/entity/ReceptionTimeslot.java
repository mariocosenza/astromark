package it.astromark.agenda.reception.entity;

import it.astromark.agenda.commons.entity.Timeslot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "reception_timeslot", schema = "astromark")
public class ReceptionTimeslot extends Timeslot {


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "reception_timetable_id", nullable = false)
    private ReceptionTimetable receptionTimetable;


    @NotNull
    @ColumnDefault("6")
    @Column(name = "capacity", nullable = false)
    private Short capacity;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "booked", nullable = false)
    private Short booked;

    @Size(max = 128)
    @NotNull
    @Column(name = "mode", nullable = false, length = 128)
    private String mode;

    @Builder.Default
    @OneToMany(mappedBy = "receptionTimeslot")
    private Set<ReceptionBooking> receptionBookings = new LinkedHashSet<>();

}