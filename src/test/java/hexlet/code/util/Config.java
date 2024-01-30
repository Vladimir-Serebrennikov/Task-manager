package hexlet.code.util;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public final class Config {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
