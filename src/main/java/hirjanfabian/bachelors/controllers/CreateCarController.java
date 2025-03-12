package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.MakesService;
import hirjanfabian.bachelors.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/createCar")
public class CreateCarController {
    private final CarService carService;
    private final MakesService makesService;
    private final UserService userService;

    public CreateCarController(CarService carService, MakesService makesService, UserService userService) {
        this.carService = carService;
        this.makesService = makesService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        CarMakes carMake = makesService.findMakeById(car.getCarMake().getId());
        CarModels carModel = makesService.findModelById(car.getCarModel().getId());
        User user = userService.findById(car.getUser().getId());

        car.setCarMake(carMake);
        car.setCarModel(carModel);
        car.setUser(user);

        carService.createCar(car);
        return ResponseEntity.ok(car);
    }
}