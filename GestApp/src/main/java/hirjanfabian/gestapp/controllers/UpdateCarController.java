package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.entities.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class UpdateCarController {

    private final CarService carService;

    public UpdateCarController(CarService carService) {
        this.carService = carService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        Car car = carService.updateCar(id, updatedCar);
        return ResponseEntity.ok(car);
    }
}