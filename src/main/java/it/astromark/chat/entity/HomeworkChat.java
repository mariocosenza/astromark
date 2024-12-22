package it.astromark.chat.entity;

import it.astromark.classwork.entity.Homework;
import it.astromark.user.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "homework_chat", schema = "astromark")
public class HomeworkChat extends Chat {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('astromark.homework_chat_homework_signed_hour_teaching_timeslot_id_seq')")
    @JoinColumn(name = "homework_signed_hour_teaching_timeslot_id", nullable = false)
    private Homework homeworkSignedHourTeachingTimeslot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Builder.Default
    @OneToMany(mappedBy = "homeworkChat")
    private Set<Message> messages = new LinkedHashSet<>();

}