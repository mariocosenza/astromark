package it.astromark;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringTestConf {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}

