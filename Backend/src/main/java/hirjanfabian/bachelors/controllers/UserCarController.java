// src/main/java/hirjanfabian/bachelors/controllers/UserCarController.java
package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.security.JwtUtil;
import hirjanfabian.bachelors.services.CarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserCarController {

    private final CarService carService;
    private final JwtUtil    jwtUtil;
    private final CarMapper  carMapper;   // ← injectat

    @GetMapping("/car")
    public CompletableFuture<ResponseEntity<CarDTO>> getUserCar(HttpServletRequest request) {

        String token     = request.getHeader("Authorization").substring(7);
        String username  = jwtUtil.extractUsername(token);

        return carService.getCarByUsername(username)
                .thenApply(car -> ResponseEntity.ok(carMapper.toDto(car)));
    }
}
