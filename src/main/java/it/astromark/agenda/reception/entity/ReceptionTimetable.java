package it.astromark.agenda.reception.entity;

import it.astromark.agenda.commons.entity.Timetable;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;


@Setter
@Getter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "reception_timetable", schema = "astromark")
public class ReceptionTimetable extends Timetable {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "text_info_reception", length = Integer.MAX_VALUE)
    private String textInfoReception;

    @Builder.Default
    @OneToMany(mappedBy = "receptionTimetable")
    private Set<ReceptionTimeslot> receptionTimeslots = new LinkedHashSet<>();

}