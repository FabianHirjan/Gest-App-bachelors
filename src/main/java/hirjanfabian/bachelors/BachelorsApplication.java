package hirjanfabian.bachelors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
@EnableAsync
@EnableCaching

public class BachelorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BachelorsApplication.class, args);
    }

}
