package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.MakesService;
import hirjanfabian.bachelors.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/car")
public class CreateCarController {
    private final CarService carService;
    private final MakesService makesService;
    private final UserService userService;

    public CreateCarController(CarService carService, MakesService makesService, UserService userService) {
        this.carService = carService;
        this.makesService = makesService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        Car car = CarMapper.toCar(carDTO);
        CarMakes carMake = makesService.findMakeById(car.getCarMake().getId());
        CarModels carModel = makesService.findModelById(car.getCarModel().getId());

        car.setCarMake(carMake);
        car.setCarModel(carModel);

        if (car.getUser() != null) {
            User user = userService.findById(car.getUser().getId());
            car.setUser(user);
            User carUser = userService.findById(car.getUser().getId());
            carUser.setCar(car);
        }

        CompletableFuture<CarDTO> createdCarDTOFuture = carService.createCar(car)
                .thenApply(CarMapper::toCarDTO);

        CarDTO createdCarDTO = createdCarDTOFuture.join();
        return ResponseEntity.ok(createdCarDTO);
    }
}