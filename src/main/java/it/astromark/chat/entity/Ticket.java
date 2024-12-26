package it.astromark.chat.entity;

import it.astromark.user.parent.entity.Parent;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "ticket", schema = "astromark")
public class Ticket extends Chat {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotBlank
    @Size(max = 64)
    @Column(name = "category", length = 64)
    private String category;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "datetime", nullable = false)
    private Instant datetime;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "closed", nullable = false)
    private Boolean closed = false;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "solved", nullable = false)
    private Boolean solved = false;

}