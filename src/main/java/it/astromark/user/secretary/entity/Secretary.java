package it.astromark.user.secretary.entity;

import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "secretary",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_secretary_username_code_tax_id", columnNames = {"username","school_code", "tax_id"})}
)
public class Secretary extends SchoolUser {

}