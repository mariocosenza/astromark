package it.astromark.classmanagement.entity.didactic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject", schema = "astromark")
public class Subject {
    @Id
    @Size(max = 64)
    @Column(name = "title", nullable = false, length = 64)
    private String title;

}