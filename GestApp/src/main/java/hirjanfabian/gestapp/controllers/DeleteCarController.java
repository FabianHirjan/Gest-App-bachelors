package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/car")
public class DeleteCarController {
    private final CarService carService;
    public DeleteCarController(CarService carService) {
        this.carService = carService;
    }

    @DeleteMapping
    public void deleteCar(@RequestParam Long id) {
        carService.deleteCar(id);
    }
}

