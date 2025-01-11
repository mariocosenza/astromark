package it.astromark.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delay", schema = "astromark")
public class Delay extends JustifiableEntity {

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date_time", nullable = false)
    private Instant date;

}