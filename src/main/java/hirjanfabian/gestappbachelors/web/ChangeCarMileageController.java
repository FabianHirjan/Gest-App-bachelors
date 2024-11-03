package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.business.CarService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/changeCarMileage")
public class ChangeCarMileageController {
    private final CarService carService;

    public ChangeCarMileageController(CarService carService) {
        this.carService = carService;
    }

    @PutMapping
    public void changeCarMileage(@RequestParam Long id, @RequestParam int newMileage) {
        carService.changeCarMileage(id, newMileage);
    }
}
