package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterController {
    private final UserService userService;


    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void registerUser(@RequestBody User user) {
        userService.registerUser(user);
    }
}
