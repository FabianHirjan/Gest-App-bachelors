package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.services.CarAlertService;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.MakesService;
import hirjanfabian.bachelors.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/car")
public class CreateCarController {
    private final CarService carService;
    private final MakesService makesService;
    private final UserService userService;
    private final CarAlertService carAlertService;

    public CreateCarController(CarService carService, MakesService makesService, UserService userService, CarAlertService carAlertService) {
        this.carService = carService;
        this.makesService = makesService;
        this.userService = userService;
        this.carAlertService = carAlertService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCar(@RequestBody CarDTO carDTO) {
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

        if(carService.getCarByLicensePlateSync(car.getLicensePlate()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        CompletableFuture<Car> createdCarFuture = carService.createCar(car);
        Car createdCar = createdCarFuture.join();
        CarDTO createdCarDTO = CarMapper.toCarDTO(createdCar);

        Map<String, Object> response = new HashMap<>();
        response.put("car", createdCarDTO);
        response.put("alerts", carAlertService.getCarAlerts(createdCar));

        return ResponseEntity.ok(response);
    }
}