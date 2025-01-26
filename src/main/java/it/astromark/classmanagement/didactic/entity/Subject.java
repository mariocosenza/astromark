package it.astromark.classmanagement.didactic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject", schema = "astromark")
public class Subject {
    @Id
    @Size(max = 64)
    @Size(min = 3)
    @NotBlank
    @Column(name = "title", nullable = false, length = 64)
    private String title;

}