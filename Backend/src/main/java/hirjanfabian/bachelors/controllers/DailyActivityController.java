package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.DailyActivityDTO;
import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.DailyActivityMapper;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.DailyActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> getAllActivities(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);
        List<DailyActivity> activities;
        if (user.getRole().equals("Admin")) {
            activities = dailyActivityService.findAllActivities();
        } else if (user.getRole().equals("User")) {
            activities = dailyActivityService.findUserActivities(user);
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
        List<DailyActivityDTO> dtos = activities.stream()
                .map(DailyActivityMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> createDaily(HttpServletRequest request,
                                         @RequestBody DailyActivityDTO dailyActivityDTO) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        // Mapăm DTO-ul la entitate
        DailyActivity activity = DailyActivityMapper.toEntity(dailyActivityDTO);
        activity.setUser(user);
        // Setează câmpuri suplimentare (de ex., approved, mașina) în service sau aici, după necesitate.
        activity.setApproved(false);
        // Dacă vrei să setezi mașina din user:
        if (user.getCar() != null) {
            activity.setCar(user.getCar());
        }
        DailyActivity savedActivity = dailyActivityService.createDailyActivity(activity);
        DailyActivityDTO savedDTO = DailyActivityMapper.toDTO(savedActivity);
        return ResponseEntity.ok(savedDTO);
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
