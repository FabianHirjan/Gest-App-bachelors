package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.DailyActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/dailyactivities")
public class DailyActivityController {

    private final DailyActivityService dailyActivityService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public DailyActivityController(DailyActivityService dailyActivityService,
                                   JwtUtil jwtUtil,
                                   UserRepository userRepository) {
        this.dailyActivityService = dailyActivityService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllActivities() {
        return ResponseEntity.ok(dailyActivityService.findAllActivities());
    }

    @PostMapping
    public ResponseEntity<?> createDaily(HttpServletRequest request,
                                         @RequestBody DailyActivity activity) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);
        activity.setUser(user);
        return ResponseEntity.ok(dailyActivityService.createDailyActivity(activity));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveDaily(@PathVariable Long id) {
        DailyActivity approved = dailyActivityService.approveActivity(id);
        if (approved != null) {
            return ResponseEntity.ok("Activity approved and car mileage updated.");
        }
        return ResponseEntity.badRequest().body("Activity not found or already approved.");
    }
}
