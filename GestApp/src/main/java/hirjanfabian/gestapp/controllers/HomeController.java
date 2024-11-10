package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.UIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final UIService uiService;

    public HomeController(UIService uiService) {
        this.uiService = uiService;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        String username = uiService.getAuthenticatedUserName();
        if (username != null) {
            return ResponseEntity.ok("Hello " + username);
        } else {
            return ResponseEntity.status(401).body("User not authenticated");
        }
    }
}