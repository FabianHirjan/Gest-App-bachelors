package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user/assignCar")
public class AssignCarController {
    private final CarService carService;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AssignCarController(CarService carService, UserService userService, JwtUtil jwtUtil) {
        this.carService = carService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> assignCar(HttpServletRequest request, @RequestBody Car car) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username);
        car.setUser(user);
        carService.saveCar(car);
        user.setCar(car);
        userService.saveUser(user);
        return ResponseEntity.ok("Car " + car.getId() + " assigned successfully" + " to user " + user.getId());
    }
}
