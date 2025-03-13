package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.Complaints;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.ComplaintsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/complaints")
public class CreateComplaintController {
    private final ComplaintsService complaintsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public CreateComplaintController(ComplaintsService complaintsService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.complaintsService = complaintsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> createComplaint(HttpServletRequest request, @RequestParam Long targetUserId, @RequestBody Complaints complaint) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User complainingUser = userRepository.findByUsername(username);
        User targetUser = userRepository.findById(targetUserId).orElse(null);
        complaint.setComplainingUser(complainingUser);
        complaint.setTargetUser(targetUser);
        complaintsService.createComplaint(complaint);
        return ResponseEntity.ok("Complaint created");
    }
}