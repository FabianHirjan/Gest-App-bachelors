package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class DeleteUserController {
    private final UserService userService;

    public DeleteUserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@RequestBody Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
