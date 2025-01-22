package it.astromark.authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "black_listed_token", schema = "astromark")
public class BlackListedToken {
    @Id
    @Size(max = 512)
    @Column(name = "token", nullable = false, length = 512)
    private String token;

}