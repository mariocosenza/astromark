package it.astromark.agenda.commons.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "start_validity", nullable = false)
    private LocalDate startValidity;

    @Column(name = "end_validity")
    private LocalDate endValidity;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Timetable timetable)) return false;
        return Objects.equals(id, timetable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
