package hirjanfabian.gestappbachelors.web;
import hirjanfabian.gestappbachelors.business.UserService;
import hirjanfabian.gestappbachelors.data.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterUserController {
   private final UserService userService;

    public RegisterUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
    }
}
