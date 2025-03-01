package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static hirjanfabian.gestapp.Routes.*;


@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(REGISTER)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // se va căuta fișierul register.html din directorul template (ex.: src/main/resources/templates)
    }

    @PostMapping(REGISTER)
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("DRIVER");
        user.setJoinDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (user.getDateOfBirth() == null && user.getDateOfBirthString() != null && !user.getDateOfBirthString().isEmpty()) {
                user.setDateOfBirth(formatter.parse(user.getDateOfBirthString()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "register";
        }

        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
