package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/user")
public class UserCarController {
    private final CarService carService;
    private final JwtUtil jwtUtil;

    public UserCarController(CarService carService, JwtUtil jwtUtil) {
        this.carService = carService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/car")
    public CompletableFuture<ResponseEntity<Car>> getUserCar(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        return carService.getCarByUsername(username)
                .thenApply(ResponseEntity::ok);
    }

}
