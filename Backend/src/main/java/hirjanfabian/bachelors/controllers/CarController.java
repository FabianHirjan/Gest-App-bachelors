package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.mapper.CarMapper;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.mapper.ModelMapper;
import hirjanfabian.bachelors.services.CarService;
import hirjanfabian.bachelors.services.MakesService;
import hirjanfabian.bachelors.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller exposing CRUD and assignment operations for {@link Car} resources.
 * <p>
 * Each endpoint returns DTOs to avoid leaking internal entity state. For concurrency, service methods are wired to
 * asynchronous counterparts returning {@link CompletableFuture}. Where nullability cannot be encoded via annotations,
 * manual checks provide early error responses.
 */
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final MakesService makesService;
    private final MakeMapper makeMapper;
    private final ModelMapper modelMapper;
    private final CarService carService;
    private final CarMapper carMapper;
    private final UserService userService;

    /*──────────── ENUMERĂRI ────────────*/

    /**
     * Lists every car make in the catalogue.
     */
    @GetMapping("/makes")
    public ResponseEntity<List<CarMakeDTO>> getAllMakes() {
        logger.info("GET /api/cars/makes");
        List<CarMakes> makes = makesService.findAllMakes();
        return ResponseEntity.ok(makes.stream()
                .map(makeMapper::toDto)
                .toList());
    }

    /**
     * Lists every car model, optionally filtered by makeId.
     */
    @GetMapping("/models")
    public ResponseEntity<List<CarModelDTO>> getModels(@RequestParam(value = "makeId", required = false) Long makeId) {
        logger.info("GET /api/cars/models?makeId={}", makeId);
        List<CarModels> models = makeId != null
                ? makesService.findModelsByMakeId(makeId)
                : makesService.findAllModels();
        return ResponseEntity.ok(models.stream()
                .map(modelMapper::toDto)
                .toList());
    }

    /**
     * Returns cars that are not yet assigned to a user.
     */
    @GetMapping("/unassigned")
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getUnassignedCars() {
        logger.info("GET /api/cars/unassigned");
        return carService.getUnassignedCars()
                .thenApply(list -> ResponseEntity.ok(
                        list.stream().map(carMapper::toDto).toList()));
    }

    /*──────────── C  •  R  •  U  •  D ────────────*/

    /**
     * Creates a new {@link Car}. The DTO is validated manually (see {@code validateDto}).
     */
    @Transactional
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        logger.info("POST /api/cars: {}", carDTO.getLicensePlate());
        String validationErr = validateDto(carDTO);
        if (validationErr != null) {
            logger.warn("Validation failed: {}", validationErr);
            return ResponseEntity.badRequest().build();
        }

        Car car = carMapper.toEntity(carDTO);

        CarMakes make = makesService.findMakeById(car.getCarMake().getId());
        CarModels model = makesService.findModelById(car.getCarModel().getId());
        car.setCarMake(make);
        car.setCarModel(model);

        if (car.getUser() != null) {
            User user = userService.findById(car.getUser().getId());
            car.setUser(user);
            user.setCar(car);
        }

        if (carService.getCarByLicensePlateSync(car.getLicensePlate()).join() != null) {
            logger.warn("Duplicate license plate: {}", car.getLicensePlate());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        CompletableFuture<CarDTO> created = carService.createCar(car).thenApply(carMapper::toDto);
        return ResponseEntity.ok(created.join());
    }

    /** Deletes the specified car. */
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        logger.info("DELETE /api/cars/{}", carId);
        if (carId == null || carId <= 0) {
            logger.warn("Invalid carId: {}", carId);
            return ResponseEntity.badRequest().build();
        }
        carService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Partially updates the license plate or estimated price of an existing car.
     */
    @PatchMapping("/{carId}")
    public ResponseEntity<Void> updateCar(@PathVariable Long carId, @RequestBody CarDTO carDto) {
        logger.info("PATCH /api/cars/{}: licensePlate={}, estimatedPrice={}",
                carId, carDto.getLicensePlate(), carDto.getEstimatedPrice());
        if (carId == null || carId <= 0) {
            logger.warn("Invalid carId: {}", carId);
            return ResponseEntity.badRequest().build();
        }
        Optional<Car> carOpt = carService.findCarById(carId);
        if (carOpt.isEmpty()) {
            logger.warn("Car with ID {} not found", carId);
            return ResponseEntity.notFound().build();
        }
        Car car = carOpt.get();
        boolean updated = false;
        if (StringUtils.hasText(carDto.getLicensePlate())) {
            car.setLicensePlate(carDto.getLicensePlate());
            updated = true;
        }
        if (carDto.getEstimatedPrice() != null) {
            car.setEstimatedPrice(carDto.getEstimatedPrice());
            updated = true;
        }
        if (!updated) {
            logger.warn("No valid fields to update for carId {}", carId);
            return ResponseEntity.badRequest().build();
        }
        carService.saveCar(car);
        return ResponseEntity.noContent().build();
    }

    /** Returns every car with an estimated price. */
    @GetMapping
    public CompletableFuture<ResponseEntity<List<CarDTO>>> getAllCars() {
        logger.info("GET /api/cars");
        return carService.getAllCars()
                .thenApply(list -> ResponseEntity.ok(
                        list.stream().map(carMapper::toDto).toList()));
    }

    /*──────────── HELPERS ────────────*/

    private String validateDto(CarDTO dto) {
        if (dto == null) return "DTO must not be null";
        if (!StringUtils.hasText(dto.getLicensePlate())) return "License plate required";
        if (dto.getYear() <= 1900) return "Invalid year";
        if (dto.getCarMake() == null || dto.getCarMake().getId() == null) return "Car make required";
        if (dto.getCarModel() == null || dto.getCarModel().getId() == null) return "Car model required";
        return null;
    }
}