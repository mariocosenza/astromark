package it.astromark.user.secretary.entity;

import it.astromark.user.commons.model.SchoolUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "secretary",
        schema = "astromark",
        uniqueConstraints = {@UniqueConstraint(name = "uk_secretary_tax_id", columnNames = "tax_id")}
)
public class Secretary extends SchoolUser {

}