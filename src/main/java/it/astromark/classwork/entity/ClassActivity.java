package it.astromark.classwork.entity;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "class_activity", schema = "astromark")
public class ClassActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_activity_id_gen")
    @SequenceGenerator(name = "class_activity_id_gen", sequenceName = "class_activity_signed_hour_teaching_timeslot_id_seq", allocationSize = 1)
    @Column(name = "signed_hour_teaching_timeslot_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.class_activity_signed_hour_teaching_timeslot_id_seq')")
    @JoinColumn(name = "signed_hour_teaching_timeslot_id", nullable = false)
    private SignedHour signedHour;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 256)
    @NotBlank
    @Column(name = "title", nullable = false, length = 256)
    private String title;

}