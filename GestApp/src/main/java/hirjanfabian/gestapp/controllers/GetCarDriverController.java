package hirjanfabian.gestapp.controllers;


import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.entities.Car;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/getCarDriver")
public class GetCarDriverController {


    private final CarService carService;

    public GetCarDriverController(CarService carService) {
        this.carService = carService;
    }


    @PutMapping("/{id}")
    public ResponseEntity<Car>
}
