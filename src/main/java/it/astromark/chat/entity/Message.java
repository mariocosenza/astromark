package it.astromark.chat.entity;

import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message", schema = "astromark")
public class Message {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_chat_id")
    private HomeworkChat homeworkChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secretary_id")
    private Secretary secretary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @Column(name = "text", length = Integer.MAX_VALUE)
    private String text;

    @Size(max = 1024)
    @Column(name = "attachment", length = 1024)
    private String attachment;

}