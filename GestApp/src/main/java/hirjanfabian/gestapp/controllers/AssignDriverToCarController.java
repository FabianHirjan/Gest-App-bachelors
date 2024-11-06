package hirjanfabian.gestapp.controller;

import hirjanfabian.gestapp.business.AssignDriverToCarService;
import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class AssignDriverToCarController {

    private final AssignDriverToCarService assignDriverToCarService;

    public AssignDriverToCarController(AssignDriverToCarService assignDriverToCarService) {
        this.assignDriverToCarService = assignDriverToCarService;
    }

    @PostMapping("/assign/{carID}/{driverID}")
    public ResponseEntity<Car> assignCar(@PathVariable Long carID, @PathVariable Long driverID) {
        Car car = new Car();
        car.setId(carID);

        User user = new User();
        user.setId(driverID);

        Car updatedCar = assignDriverToCarService.assignCarToDriver(car, user);

        if (updatedCar != null) {
            return ResponseEntity.ok(updatedCar);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
