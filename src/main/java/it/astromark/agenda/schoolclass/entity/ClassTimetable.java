package it.astromark.agenda.schoolclass.entity;

import it.astromark.agenda.commons.entity.RedDate;
import it.astromark.agenda.commons.entity.Timetable;
import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "class_timetable",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_class_timetable", columnNames = {"school_class_id", "start_validity", "end_validity"})}
)
public class ClassTimetable extends Timetable {
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ColumnDefault("nextval('astromark.class_timetable_school_class_id_seq')")
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @NotNull
    @ColumnDefault("27")
    @Min(0)
    @Max(40)
    @Column(name = "expected_hours", nullable = false)
    private Short expectedHours;

    @Builder.Default
    @OneToMany(mappedBy = "classTimetable")
    private Set<RedDate> redDates = new LinkedHashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "classTimetable")
    private Set<TeachingTimeslot> teachingTimeslots = new LinkedHashSet<>();

}