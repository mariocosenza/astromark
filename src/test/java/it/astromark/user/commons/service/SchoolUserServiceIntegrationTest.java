package it.astromark.user.commons.service;

import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.commons.configuration.SpringValidationConf;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.repository.TeacherRepository;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(roles="student")
@Import({SpringTestConf.class, SpringValidationConf.class})
class SchoolUserServiceIntegrationTest {

    @Inject
    private  AuthenticationService authenticationService;
    @Inject
    private  StudentRepository studentRepository;
    @Inject
    private  TeacherRepository teacherRepository;
    @Inject
    private  SchoolUserMapper schoolUserMapper;
    @Inject
    private  SecretaryRepository secretaryRepository;
    @Inject
    private  ParentRepository parentRepository;
    @Inject
    private SchoolUserServiceImpl schoolUserService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");


    @BeforeEach
    void setUp() {

    }

    @Test
    void tc2_01() {

    }

    @Test
    void tc2_02() {
    }

    @Test
    void tc2_03() {
    }

    @Test
    void tc2_04() {
    }

    @Test
    void tc2_05() {
    }

    @Test
    void tc3_01() {
    }

    @Test
    void tc3_02() {
    }

    @Test
    void tc3_03() {
    }

    @Test
    void tc3_04() {
    }

    @Test
    void tc3_05() {
    }

}