package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.business.AssignCarService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assignCar")
public class AssignCarController {
    private final AssignCarService assignCarService;

    public AssignCarController(AssignCarService assignCarService) {
        this.assignCarService = assignCarService;
    }

    @PostMapping
    public void assignCar(@RequestParam Long carId, @RequestParam Long employeeId) {
        assignCarService.assignCar(carId, employeeId);
    }
}