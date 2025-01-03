package it.astromark.agenda.commons.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Min(value = 1, message = "Valid range 1 to 8")
    @Max(value = 8, message = "Valid range 1 to 8")
    @Column(name = "hour", nullable = false)
    private Short hour;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Timeslot timeslot)) return false;
        return Objects.equals(id, timeslot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
