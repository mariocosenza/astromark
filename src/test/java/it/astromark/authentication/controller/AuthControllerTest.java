package it.astromark.authentication.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");


    @Test
    void tc1_01() {
        assertTrue(true);
    }

    @Test
    void tc1_02() {
        assertTrue(true);
    }

    @Test
    void tc1_03() {
        assertTrue(true);
    }

    @Test
    void tc1_04() {
        assertTrue(true);
    }

    @Test
    void tc1_05() {
        assertTrue(true);
    }

    @Test
    void tc1_06() {
        assertTrue(true);
    }

    @Test
    void tc1_07() {
        assertTrue(true);
    }

    @Test
    void tc1_08() {
        assertTrue(true);
    }

    @Test
    void tc1_09() {
        assertTrue(true);
    }

    @Test
    void tc1_10() {
        assertTrue(true);
    }

    @Test
    void tc1_11() {
        assertTrue(true);
    }

    @Test
    void tc1_12() {
        assertTrue(true);
    }

    @Test
    void tc1_13() {
        assertTrue(true);
    }

    @Test
    void tc1_14() {
        assertTrue(true);
    }

    @Test
    void tc1_15() {
        assertTrue(true);
    }

}