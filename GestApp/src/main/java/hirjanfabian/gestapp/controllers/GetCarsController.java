package hirjanfabian.gestapp.controllers;


import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.entities.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetCarsController {
    private final CarService carService;

    public GetCarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/api/cars")
    public ResponseEntity<List<Car>> getCars() {
        List<Car> cars = carService.all();
        return ResponseEntity.ok(cars);
    }


}
