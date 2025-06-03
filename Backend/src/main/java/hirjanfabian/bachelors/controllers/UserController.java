package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.dto.LocationUpdateDTO;
import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.mapper.UserMapper;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.UserService;
import hirjanfabian.bachelors.websocket.UserTrackingWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final CarService carService;
    private final CarMapper carMapper;


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        List<UserDTO> users = userService.findAllExcept(username).stream()
                .map(UserMapper::toDTO)
                .toList();
        return ResponseEntity.ok(users);
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

    @GetMapping("/locations")
    public ResponseEntity<List<LocationUpdateDTO>> getAllUsersLocations(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String currentUsername = jwtUtil.extractUsername(token);
        User currentUser = userRepository.findByUsername(currentUsername);

        if (currentUser == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<User> users = userRepository.findAll();
        List<LocationUpdateDTO> locations = users.stream()
                .filter(user -> user.getLastLatitude() != null && user.getLastLongitude() != null)
                .map(user -> {
                    LocationUpdateDTO dto = new LocationUpdateDTO();
                    dto.setLatitude(user.getLastLatitude());
                    dto.setLongitude(user.getLastLongitude());
                    dto.setUsername(user.getUsername()); // Assuming you add username to LocationUpdateDTO
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(locations);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/car")
    public CompletableFuture<ResponseEntity<CarDTO>> getUserCar(HttpServletRequest request) {

        String token     = request.getHeader("Authorization").substring(7);
        String username  = jwtUtil.extractUsername(token);

        return carService.getCarByUsername(username)
                .thenApply(car -> ResponseEntity.ok(carMapper.toDto((Car) car)));
    }


    @PostMapping("unassignCar")
    public ResponseEntity<String> unassignCar(HttpServletRequest request, @RequestBody Car car) {
        Long carId = car.getId();
        if (carId == null) {
            return ResponseEntity.badRequest().body("Car ID is required");
        }

        try {
            // Fetch the full Car entity from the database
            Car existingCar = carService.findCarById(carId)
                    .orElseThrow(() -> new RuntimeException("Car not found with ID: " + carId));
            User user = existingCar.getUser();
            user.setCar(null);
            userService.saveUser(user);

            // Update only the user_id to null to unassign
            existingCar.setUser(null); // Assuming Car has a User field or user_id

            // Save the updated Car entity
            carService.saveCar(existingCar);


            return ResponseEntity.ok("Car " + carId + " unassigned successfully from user");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error unassigning car: " + e.getMessage());
        }
    }

    @PostMapping("assignCar")
    public ResponseEntity<String> assignCar(@RequestBody Map<String, Long> payload) {
        Long carId  = payload.get("carId");
        Long userId = payload.get("userId");

        if (carId == null || userId == null) {
            return ResponseEntity.badRequest().body("carId and userId are required");
        }

        try {
            Car car = carService.findCarById(carId)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

            car.setUser(user);
            carService.saveCar(car);

            user.setCar(car);
            userService.saveUser(user);

            return ResponseEntity.ok("Car " + carId + " assigned to user " + userId);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }


}