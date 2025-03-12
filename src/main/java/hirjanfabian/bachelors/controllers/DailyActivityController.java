package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.respositories.UserRepository;
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

    // GET /api/dailyactivities
    @GetMapping
    public ResponseEntity<?> getAllActivities() {
        return ResponseEntity.ok(dailyActivityService.findAllActivities());
    }

    // POST /api/dailyactivities
    @PostMapping
    public ResponseEntity<?> createDaily(HttpServletRequest request,
                                         @RequestBody DailyActivity activity) {
        // 1. Extrage token-ul din Header ("Authorization: Bearer <token>")
        String token = request.getHeader("Authorization").substring(7);
        // 2. Extrage username-ul din token
        String username = jwtUtil.extractUsername(token);
        // 3. Găsește user-ul în DB
        User user = userRepository.findByUsername(username);
        // 4. Setează user-ul în activitate
        activity.setUser(user);
        // (opțional) Poți seta și data curentă dacă nu o primești din front:
        // activity.setDate(LocalDate.now());

        // 5. Salvezi în DB
        return ResponseEntity.ok(dailyActivityService.createDailyActivity(activity));
    }

    // POST /api/dailyactivities/approve/{id}
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveDaily(@PathVariable Long id) {
        DailyActivity approved = dailyActivityService.approveActivity(id);
        if (approved != null) {
            return ResponseEntity.ok("Activity approved and car mileage updated.");
        }
        return ResponseEntity.badRequest().body("Activity not found or already approved.");
    }
}
