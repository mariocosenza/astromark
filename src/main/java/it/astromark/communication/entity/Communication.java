package it.astromark.communication.entity;

import it.astromark.classmanagement.entity.SchoolClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "communication", schema = "astromark")
public class Communication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "communication_id_gen")
    @SequenceGenerator(name = "communication_id_gen", sequenceName = "communication_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ColumnDefault("nextval('astromark.communication_school_class_id_seq')")
    @JoinColumn(name = "school_class_id", nullable = false)
    private SchoolClass schoolClass;

    @Size(max = 256)
    @NotBlank
    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @ColumnDefault("''")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("now()")
    @Column(name = "date")
    private LocalDate date;

}