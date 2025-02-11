package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.entities.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/getCarDriver")
public class GetCarDriverController {

    private final CarService carService;

    public GetCarDriverController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Long> getCarDriver(@RequestParam("carID") Long carID) {
        // Căutăm mașina după id-ul primit
        Car car = carService.findById(carID);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        if (car.getDriver() == null) {
            return ResponseEntity.noContent().build();
        }
        // Returnăm id-ul şoferului asociat maşinii
        return ResponseEntity.ok(car.getDriver().getId());
    }
}
