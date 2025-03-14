package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class GetUnassignedCarsController {
    private final CarService carService;

    public GetUnassignedCarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/unassigned")
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getUnassignedCars() {
        return carService.getUnassignedCars()
                .thenApply(cars -> {
                    List<CarDTO> carDTOs = cars.stream()
                            .map(CarMapper::toCarDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(carDTOs);
                });
    }


}
