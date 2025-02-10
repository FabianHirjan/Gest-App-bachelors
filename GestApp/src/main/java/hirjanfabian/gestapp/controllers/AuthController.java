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

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // se va căuta fișierul register.html din directorul template (ex.: src/main/resources/templates)
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        // Encodează parola
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Setează rolul implicit (poate fi USER sau altceva după necesitate)
        user.setRole("USER");
        // Setează data de înregistrare la data curentă (la începutul zilei)
        user.setJoinDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Dacă data de naștere nu a fost setată automat (prin binding), încearcă să o parsezi din datele primite
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (user.getDateOfBirth() == null && user.getDateOfBirthString() != null && !user.getDateOfBirthString().isEmpty()) {
                user.setDateOfBirth(formatter.parse(user.getDateOfBirthString()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Dacă apare o eroare, rămâi pe pagina de înregistrare (poți afișa și un mesaj de eroare)
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
