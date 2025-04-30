// src/main/java/hirjanfabian/bachelors/controllers/CreateCarController.java
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CreateCarController {

    private final CarService  carService;
    private final MakesService makesService;
    private final UserService  userService;
    private final CarMapper    carMapper;

    @Transactional
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {

        Car car = carMapper.toEntity(carDTO);

        CarMakes  make  = makesService.findMakeById(car.getCarMake().getId());
        CarModels model = makesService.findModelById(car.getCarModel().getId());
        car.setCarMake(make);
        car.setCarModel(model);

        if (car.getUser() != null) {
            User user = userService.findById(car.getUser().getId());
            car.setUser(user);
            user.setCar(car);                       // relație bidirecțională
        }

        // placă existentă?
        if (carService.getCarByLicensePlateSync(car.getLicensePlate()).join() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        CompletableFuture<CarDTO> created =
                carService.createCar(car).thenApply(carMapper::toDto);

        return ResponseEntity.ok(created.join());
    }
}
