package it.astromark.rating.serivice;

import it.astromark.SpringTestConf;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ActiveProfiles(value = "test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({SpringTestConf.class})
class MarkServiceIntegrationTest {
    @Test
    void tc5_01() {

    }

    @Test
    void tc2_02() {

    }

    @Test
    void tc5_03() {

    }

    @Test
    void tc5_04() {

    }

    @Test
    void tc5_05() {

    }

    @Test
    void tc5_06() {

    }

    @Test
    void tc5_07() {

    }

}