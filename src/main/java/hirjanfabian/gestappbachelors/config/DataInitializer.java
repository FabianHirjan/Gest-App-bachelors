package hirjanfabian.gestappbachelors.config;

import hirjanfabian.gestappbachelors.business.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            userService.registerUser("admin", "password123");
        };
    }
}