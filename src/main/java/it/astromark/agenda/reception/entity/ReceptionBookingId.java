package it.astromark.agenda.reception.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionBookingId implements Serializable {
    @Serial
    private static final long serialVersionUID = -6673933355580646698L;
    @NotNull
    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @NotNull
    @Column(name = "reception_timeslot_id", nullable = false)
    private Integer receptionTimeslotId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReceptionBookingId entity = (ReceptionBookingId) o;
        return Objects.equals(this.receptionTimeslotId, entity.receptionTimeslotId) &&
                Objects.equals(this.parentId, entity.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receptionTimeslotId, parentId);
    }

}