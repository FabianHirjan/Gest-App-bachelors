package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.business.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/createCar")
public class CreateCarController {

    private final CarService carService;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CreateCarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCar(@RequestParam String make,
                                                         @RequestParam String model,
                                                         @RequestParam String dateOfProduction,
                                                         @RequestParam int mileage,
                                                         @RequestParam String registrationPlate,
                                                         @RequestParam boolean insuranceAvailable,
                                                         @RequestParam String insuranceExpirationDate) {
        LocalDate productionDate = LocalDate.parse(dateOfProduction, dateFormat);
        carService.createCar(make, model, productionDate.getYear(), mileage, registrationPlate, productionDate, insuranceAvailable, String.valueOf(LocalDate.parse(insuranceExpirationDate, dateFormat)));

        // Create JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Car created successfully!");

        return new ResponseEntity<>(response, HttpStatus.CREATED); // HTTP 201 success code
    }
}