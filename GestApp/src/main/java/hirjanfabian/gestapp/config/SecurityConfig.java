package hirjanfabian.gestapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Disable CSRF protection
                .authorizeRequests()
                // Permite accesul pentru rutele publice
                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                // Permite accesul la /api/cars doar pentru utilizatori autentificați
                .requestMatchers("/api/cars/**").authenticated()
                // Protejează orice altă rută
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();  // Permite login-ul fără restricții
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


