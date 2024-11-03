package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.data.Car;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import hirjanfabian.gestappbachelors.business.CarService;

@Controller
@RequestMapping("/cars")
public class GetCarsController {
    private final CarService carService;

    public GetCarsController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String getCars(Model model) {
        Iterable<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "cars"; // Numele template-ului Thymeleaf
    }
}