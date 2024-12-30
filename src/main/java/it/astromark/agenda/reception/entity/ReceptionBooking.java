package it.astromark.agenda.reception.entity;

import it.astromark.user.parent.entity.Parent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reception_booking", schema = "astromark")
public class ReceptionBooking {
    @SequenceGenerator(name = "reception_booking_id_gen", sequenceName = "reception_timeslot_id_seq", allocationSize = 1)
    @EmbeddedId
    private ReceptionBookingId id;

    @MapsId("parentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @MapsId("receptionTimeslotId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.reception_booking_reception_timeslot_id_seq')")
    @JoinColumn(name = "reception_timeslot_id", nullable = false)
    private ReceptionTimeslot receptionTimeslot;

    @NotNull
    @Column(name = "booking_order", nullable = false)
    private Short bookingOrder;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "refused", nullable = false)
    private Boolean refused = false;

}