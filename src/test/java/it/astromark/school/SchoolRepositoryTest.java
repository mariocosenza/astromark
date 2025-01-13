package it.astromark.school;

import it.astromark.SpringTestConf;
import it.astromark.commons.configuration.SpringValidationConf;
import it.astromark.school.entity.School;
import it.astromark.school.repository.SchoolRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles(value = "test")
@Import({SpringTestConf.class, SpringValidationConf.class})
@Slf4j
class SchoolRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private Faker faker;

    @Test
    void save() {
        assertNotNull(schoolRepository.save(School.builder()
                .code("SS23456")
                .name(faker.name().fullName())
                .phoneNumber(faker.number().randomNumber())
                .address(faker.address().streetAddress())
                .email(faker.internet().emailAddress()).build()));
    }

}