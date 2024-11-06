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
import java.time.LocalDateTime;
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
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Set default role
        user.setJoinDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Ensure `user.getDateOfBirth()` is parsed correctly if it's passed as a String
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Assuming the dateOfBirth field is a Date in the User class:
            if (user.getDateOfBirth() != null) {
                // No parsing needed if it is already a Date type
            } else {
                // Handle String to Date parsing if necessary
                String dateOfBirthStr = user.getDateOfBirthString(); // hypothetical getter for String
                user.setDateOfBirth(formatter.parse(dateOfBirthStr));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "register"; // Return to the registration form in case of error
        }

        userRepository.save(user);
        return "redirect:/login";
    }



    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
