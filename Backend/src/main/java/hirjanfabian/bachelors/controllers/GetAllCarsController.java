// src/main/java/hirjanfabian/bachelors/controllers/GetAllCarsController.java
package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class GetAllCarsController {

    private final CarService carService;
    private final CarMapper  carMapper;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getAllCars() {
        return carService.getAllCars()          // CompletableFuture<List<Car>>
                .thenApply(list -> ResponseEntity.ok(
                        list.stream().map(carMapper::toDto).toList()));
    }
}
