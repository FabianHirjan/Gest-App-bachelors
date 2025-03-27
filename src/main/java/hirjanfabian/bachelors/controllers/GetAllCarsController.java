package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class GetAllCarsController {
    private final CarService carService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getAllCars() {
        return carService.getAllCars()
                .thenApply(cars -> {
                    List<CarDTO> carDTOs = cars.stream()
                            .map(CarMapper::toCarDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(carDTOs);
                });
    }
}
