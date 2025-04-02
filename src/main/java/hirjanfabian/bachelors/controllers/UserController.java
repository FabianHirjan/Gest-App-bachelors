package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.LocationUpdateDTO;
import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.websocket.UserTrackingWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/search")
    public List<UserDTO> searchUsers(HttpServletRequest request, @RequestParam String username) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUsername = jwtUtil.extractUsername(token);
        User currentUser = userRepository.findByUsername(currentUsername);

        List<User> found = userRepository.findByUsernameContainingIgnoreCase(username);

        found = found.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .toList();

        return found.stream()
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(u.getId());
                    dto.setUsername(u.getUsername());
                    dto.setEmail(u.getEmail());
                    dto.setRole(u.getRole());
                    dto.setFirstName(u.getFirstName());
                    dto.setLastName(u.getLastName());
                    return dto;
                })
                .toList();
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

        user.setLastLatitude(locationDTO.getLatitude());
        user.setLastLongitude(locationDTO.getLongitude());
        user.setLastLocationTimestamp(LocalDateTime.now());
        userRepository.save(user);

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("userId", user.getId());
            message.put("username", user.getUsername());
            message.put("latitude", locationDTO.getLatitude());
            message.put("longitude", locationDTO.getLongitude());
            message.put("timestamp", LocalDateTime.now().toString());
            String jsonMessage = objectMapper.writeValueAsString(message);
            UserTrackingWebSocketHandler.broadcast(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        LocationUpdateDTO dto = new LocationUpdateDTO();
        dto.setLatitude(user.getLastLatitude());
        dto.setLongitude(user.getLastLongitude());
        return ResponseEntity.ok(dto);
    }
}