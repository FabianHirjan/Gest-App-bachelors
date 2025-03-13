package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class GetUsersController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public GetUsersController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        List<User> users = userService.findAllExcept(username);
        return ResponseEntity.ok(users);
    }
}
