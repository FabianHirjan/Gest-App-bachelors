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
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", existingUser.getRole(),
                    "userId", existingUser.getId()
            ));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid or missing token");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String username = jwtUtil.extractUsername(token);
            if (jwtUtil.validateToken(token)) { // Adjusted to match single-argument method
                User user = userService.findByUsername(username);
                if (user != null) {
                    return ResponseEntity.ok(Map.of(
                            "username", user.getUsername(),
                            "role", user.getRole(),
                            "userId", user.getId()
                    ));
                }
                return ResponseEntity.status(404).body("User not found");
            }
            return ResponseEntity.status(401).body("Token is invalid or expired");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token validation failed: " + e.getMessage());
        }
    }
}