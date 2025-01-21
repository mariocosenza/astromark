package it.astromark.chat.entity;

import it.astromark.user.parent.entity.Parent;
import it.astromark.user.secretary.entity.Secretary;
import it.astromark.user.student.entity.Student;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message", schema = "astromark")
public class Message {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @NotBlank
    @Column(name = "text", length = Integer.MAX_VALUE)
    private String text;

    @Size(max = 1024)
    @Column(name = "attachment", length = 1024)
    private String attachment;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message message)) return false;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}