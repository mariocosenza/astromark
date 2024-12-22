package it.astromark.agenda.schoolclass.entity;

import it.astromark.agenda.commons.entity.RedDate;
import it.astromark.agenda.commons.entity.Timetable;
import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "class_timetable", schema = "astromark")
public class ClassTimetable extends Timetable {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.class_timetable_school_class_id_seq')")
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @NotNull
    @ColumnDefault("27")
    @Column(name = "expected_hours", nullable = false)
    private Short expectedHours;

    @Builder.Default
    @OneToMany(mappedBy = "classTimetable")
    private Set<RedDate> redDates = new LinkedHashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "classTimetable")
    private Set<TeachingTimeslot> teachingTimeslots = new LinkedHashSet<>();

}