package hirjanfabian.gestapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Security configuration class responsible for defining the security settings of the application.
 * This class configures authentication, authorization, and other security-related settings.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     * This method defines security settings such as CSRF protection, permitted and authenticated URL patterns,
     * as well as login page configuration.
     *
     * @param http the {@link HttpSecurity} object used to configure security.
     * @return a {@link SecurityFilterChain} instance representing the built security configuration.
     * @throws Exception if any error occurs during the configuration process.
     */
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
                        .loginPage("/login")          // Pagina custom de login
                        .loginProcessingUrl("/perform_login") // URL-ul la care se procesează login-ul
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                );
        return http.build();
    }



    /**
     * Creates and provides a bean of type PasswordEncoder to encode passwords securely.
     * This method returns an instance of BCryptPasswordEncoder, which implements a strong
     * hashing mechanism to safeguard passwords.
     *
     * @return a PasswordEncoder instance using the BCrypt hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


