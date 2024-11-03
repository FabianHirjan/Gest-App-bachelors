package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.business.CarService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class DeleteCarController {
    private final CarService carService;

    public DeleteCarController(CarService carService) {
        this.carService = carService;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCar(@PathVariable Long id) {
        this.carService.deleteCar(id);
    }
}