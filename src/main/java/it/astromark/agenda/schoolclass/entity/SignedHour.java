package it.astromark.agenda.schoolclass.entity;

import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "signed_hour", schema = "astromark")
public class SignedHour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signed_hour_id_gen")
    @SequenceGenerator(name = "signed_hour_id_gen", sequenceName = "signed_hour_teaching_timeslot_id_seq", allocationSize = 1)
    @Column(name = "teaching_timeslot_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teaching_timeslot_id", nullable = false)
    private TeachingTimeslot teachingTimeslot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "time_sign", nullable = false)
    private Instant timeSign;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "substitution", nullable = false)
    private Boolean substitution = false;

}