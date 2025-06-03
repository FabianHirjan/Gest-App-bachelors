package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.DailyActivityDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.DailyActivityMapper;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.DailyActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-layer gateway for CRUD-style operations on {@link DailyActivity} resources.
 * <p>
 * The caller is identified from the <em>Bearer</em> JWT in the
 * {@code Authorization} header; role-based routing decides whether the caller
 * receives all activities (<em>Admin</em>) or only their own (<em>User</em>).
 * </p>
 */
@RestController
@RequestMapping("/api/dailyactivities")
@RequiredArgsConstructor
@Slf4j
public class DailyActivityController {

    private static final String BEARER = "Bearer ";

    private final DailyActivityService dailyActivityService;
    private final JwtUtil              jwtUtil;
    private final UserRepository       userRepository;
    private final CarRepository        carRepository;

    /*────────────────── READ ──────────────────*/

    @GetMapping
    public ResponseEntity<?> getAllActivities(HttpServletRequest request) {
        User caller = extractUser(request);
        if (caller == null) {
            return ResponseEntity.badRequest().body("Invalid or missing token.");
        }

        List<DailyActivity> activities = switch (caller.getRole()) {
            case "Admin" -> dailyActivityService.findAllActivities();
            case "User"  -> dailyActivityService.findUserActivities(caller);
            default      -> null;
        };

        if (activities == null) {
            return ResponseEntity.badRequest().body("User role not recognised.");
        }

        return ResponseEntity.ok(
                activities.stream().map(DailyActivityMapper::toDTO).toList());
    }

    /*────────────────── CREATE ──────────────────*/

    @PostMapping
    public ResponseEntity<?> createDaily(HttpServletRequest request,
                                         @RequestBody DailyActivityDTO dto) {
        User caller = extractUser(request);
        if (caller == null) {
            return ResponseEntity.badRequest().body("Invalid or missing token.");
        }

        DailyActivity activity = DailyActivityMapper.toEntity(dto);
        activity.setUser(caller);          // link the creator
        activity.setApproved(false);

        // car precedence: DTO-provided ID ➜ user's own car
        if (dto.getCarId() != null) {
            Car car = carRepository.findById(dto.getCarId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Car not found: " + dto.getCarId()));
            activity.setCar(car);
        } else if (caller.getCar() != null) {
            activity.setCar(caller.getCar());
        }

        DailyActivity saved = dailyActivityService.createDailyActivity(activity);
        return ResponseEntity.ok(DailyActivityMapper.toDTO(saved));
    }

    /*────────────────── APPROVE ──────────────────*/

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveDaily(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid activity id.");
        }
        DailyActivity approved = dailyActivityService.approveActivity(id);
        return approved != null
                ? ResponseEntity.ok("Activity approved and car mileage updated.")
                : ResponseEntity.badRequest()
                .body("Activity not found or already approved.");
    }

    /*────────────────── HELPERS ──────────────────*/

    private User extractUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BEARER)) return null;

        String token    = header.substring(BEARER.length());
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username);
    }
}
