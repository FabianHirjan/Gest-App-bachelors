package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.UserService;
import hirjanfabian.bachelors.utils.PasswordUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && PasswordUtil.checkPassword(user.getPassword(), existingUser.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "role", existingUser.getRole()));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
