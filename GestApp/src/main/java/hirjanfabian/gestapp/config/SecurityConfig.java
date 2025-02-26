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
                .csrf(csrf -> csrf.disable()) // Dezactivează CSRF (atenție la implicațiile de securitate)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login.html", "/register.html", "/register", "/misc/styles.css", "/js/**").permitAll()
                        .requestMatchers("/api/cars/**", "/api/dac/**", "/api/complaint", "/cars/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")          // Pagina custom de login
                        .loginProcessingUrl("/perform_login") // URL-ul la care se procesează login-ul
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                );
        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


