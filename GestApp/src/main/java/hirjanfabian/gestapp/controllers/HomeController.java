package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.UIService;
import hirjanfabian.gestapp.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class HomeController {

    private final UIService uiService;

    public HomeController(UIService uiService) {
        this.uiService = uiService;
    }

    // Endpoint existent, de exemplu, pentru afișarea unui mesaj de întâmpinare
    @GetMapping("/home")
    public String home() {
        String username = uiService.getAuthenticatedUserName();
        if (username != null) {
            return "Hello, " + username;
        }
        return "User not authenticated";
    }

    // Nou: Endpoint pentru a obține informațiile despre utilizator (nume și rol)
    @GetMapping("/user-info")
    public Map<String, String> getUserInfo() {
        String username = uiService.getAuthenticatedUserName();
        String role = uiService.getAuthenticatedUserRole();
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("role", role);
        return data;
    }

    @GetMapping("/user-id")
    public ResponseEntity<User> getUserId() {
        Optional<User> optionalUser = uiService.getUserId();
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
