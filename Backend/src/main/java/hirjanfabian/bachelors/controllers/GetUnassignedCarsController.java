// src/main/java/hirjanfabian/bachelors/controllers/GetUnassignedCarsController.java
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
public class GetUnassignedCarsController {

    private final CarService carService;
    private final CarMapper  carMapper;

    @GetMapping("/unassigned")
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getUnassignedCars() {
        return carService.getUnassignedCars()
                .thenApply(list -> ResponseEntity.ok(
                        list.stream().map(carMapper::toDto).toList()));
    }
}
