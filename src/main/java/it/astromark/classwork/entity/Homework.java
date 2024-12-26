package it.astromark.classwork.entity;

import it.astromark.agenda.schoolclass.entity.SignedHour;
import it.astromark.chat.entity.HomeworkChat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "homework", schema = "astromark")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "homework_id_gen")
    @SequenceGenerator(name = "homework_id_gen", sequenceName = "homework_signed_hour_teaching_timeslot_id_seq", allocationSize = 1)
    @Column(name = "signed_hour_teaching_timeslot_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.homework_signed_hour_teaching_timeslot_id_seq')")
    @JoinColumn(name = "signed_hour_teaching_timeslot_id", nullable = false)
    private SignedHour signedHour;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 256)
    @NotBlank
    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "homeworkSignedHourTeachingTimeslot")
    private Set<HomeworkChat> homeworkChats = new LinkedHashSet<>();

}