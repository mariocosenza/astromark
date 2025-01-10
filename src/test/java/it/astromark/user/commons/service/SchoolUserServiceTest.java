package it.astromark.user.commons.service;

import it.astromark.SpringTestConf;
import it.astromark.authentication.service.AuthenticationService;
import it.astromark.commons.validator.SpringValidationConf;
import it.astromark.user.commons.mapper.SchoolUserMapper;
import it.astromark.user.parent.repository.ParentRepository;
import it.astromark.user.secretary.repository.SecretaryRepository;
import it.astromark.user.student.repository.StudentRepository;
import it.astromark.user.teacher.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles="student")
@Import({SpringTestConf.class, SpringValidationConf.class})
class SchoolUserServiceTest {

    @Mock
    private  AuthenticationService authenticationService;
    @Mock
    private  StudentRepository studentRepository;
    @Mock
    private  TeacherRepository teacherRepository;
    @Mock
    private  SchoolUserMapper schoolUserMapper;
    @Mock
    private  SecretaryRepository secretaryRepository;
    @Mock
    private  ParentRepository parentRepository;
    @InjectMocks
    private SchoolUserServiceImpl schoolUserService;

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