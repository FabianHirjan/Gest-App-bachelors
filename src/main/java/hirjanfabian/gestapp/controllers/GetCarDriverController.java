package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.entities.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/getCarDriver")
public class GetCarDriverController {

    private final CarService carService;

    public GetCarDriverController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Long> getCarDriver(@RequestParam("carID") Long carID) {
        Optional<Car> carOpt = carService.findById(carID);
        if (!carOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Car car = carOpt.get();
        if (car.getDriver() == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(car.getDriver().getId());
    }

}
