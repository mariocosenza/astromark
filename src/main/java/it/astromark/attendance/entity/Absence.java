package it.astromark.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "absence", schema = "astromark")
public class Absence extends JustifiableEntity {

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date", nullable = false)
    private LocalDate date;

}