package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.LocationUpdateDTO;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/location")
    public ResponseEntity<?> updateLiveLocation(HttpServletRequest request,
                                                @RequestBody LocationUpdateDTO locationDTO) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // Update last location
        user.setLastLatitude(locationDTO.getLatitude());
        user.setLastLongitude(locationDTO.getLongitude());
        user.setLastLocationTimestamp(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.ok("Location updated successfully.");
    }

    @GetMapping("/last-location")
    public ResponseEntity<?> getLastLocation(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        if (user == null || user.getLastLatitude() == null || user.getLastLongitude() == null) {
            return ResponseEntity.badRequest().body("No last location available.");
        }

        return ResponseEntity.ok(new LocationUpdateDTO() {{
            setLatitude(user.getLastLatitude());
            setLongitude(user.getLastLongitude());
        }});
    }
}