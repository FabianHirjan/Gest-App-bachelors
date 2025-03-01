package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.AssignDriverToCarService;
import hirjanfabian.gestapp.business.CarService;
import hirjanfabian.gestapp.business.UserService;
import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static hirjanfabian.gestapp.Routes.*;


@Controller
@RequestMapping(CARS)
public class CarManagementController {

    private final CarService carService;
    private final UserService userService;
    private final AssignDriverToCarService assignDriverToCarService;

    public CarManagementController(CarService carService,
                                   UserService userService,
                                   AssignDriverToCarService assignDriverToCarService) {
        this.carService = carService;
        this.userService = userService;
        this.assignDriverToCarService = assignDriverToCarService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping()
    public String showCarManagementPage(Model model) {
        List<Car> cars = carService.getAllCars();
        List<User> drivers = userService.getAllUsers();
        int totalAlerts = calculateTotalAlerts(cars);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();

        model.addAttribute("car", new Car());
        model.addAttribute("cars", cars);
        model.addAttribute("drivers", drivers);
        model.addAttribute("totalAlerts", totalAlerts);
        model.addAttribute("today", today);
        model.addAttribute("oneYearAgo", oneYearAgo);

        return "carManagement";
    }

    @PostMapping
    public String createCar(@ModelAttribute("car") Car car,
                            RedirectAttributes ra) {
        Optional<Car> createdCar = carService.createCar(car);
        if (createdCar.isPresent()) {
            ra.addFlashAttribute("message", "Car created successfully!");
        } else {
            ra.addFlashAttribute("error", "Error creating car.");
        }
        return "redirect:/cars/view";
    }

    @PostMapping(UPDATE)
    public String updateCar(@ModelAttribute Car car, RedirectAttributes ra) {
        Optional<Car> updatedCarOpt = carService.updateCar(car.getId(), car);
        if (updatedCarOpt.isPresent()) {
            ra.addFlashAttribute("message", "Car updated successfully!");
        } else {
            ra.addFlashAttribute("error", "Car not found for update.");
        }
        return "redirect:/cars";
    }

    // DELETE Car
    @PostMapping(DELETE)
    public String deleteCar(@RequestParam("id") Long id, RedirectAttributes ra) {
        boolean deleted = carService.deleteCar(id);
        if (deleted) {
            ra.addFlashAttribute("message", "Car deleted successfully!");
        } else {
            ra.addFlashAttribute("error", "Car not found for deletion.");
        }
        return "redirect:/cars/";
    }

    // ASSIGN driver
    @PostMapping(ASSIGN)
    public String assignDriver(@RequestParam("carID") Long carID,
                               @RequestParam("driverID") Long driverID,
                               RedirectAttributes ra) {
        Optional<Car> carOpt = carService.findById(carID);
        Optional<User> driverOpt = Optional.ofNullable(userService.getUserById(driverID));

        if (!carOpt.isPresent() || !driverOpt.isPresent()) {
            ra.addFlashAttribute("error", "Car or Driver not found.");
            return "redirect:/cars/view";
        }

        Car updatedCar = assignDriverToCarService.assignCarToDriver(carOpt.get(), driverOpt.get());
        if (updatedCar != null) {
            ra.addFlashAttribute("message", "Driver assigned successfully!");
        } else {
            ra.addFlashAttribute("error", "Error assigning driver.");
        }
        return "redirect:/cars/view";
    }


    // UNASSIGN driver
    @PostMapping("/unassign/{carID}")
    public String unassignDriver(@PathVariable Long carID,
                                 RedirectAttributes ra) {
        Optional<Car> carOpt = carService.findById(carID);
        if (!carOpt.isPresent()) {
            ra.addFlashAttribute("error", "Car not found.");
            return "redirect:/cars/view";
        }

        Car car = carOpt.get();
        Car updatedCar = assignDriverToCarService.unasignCarFromDriver(car);
        if (updatedCar != null) {
            ra.addFlashAttribute("message", "Driver unassigned successfully!");
        } else {
            ra.addFlashAttribute("error", "Error unassigning driver.");
        }
        return "redirect:/cars/view";
    }

    // Metodă helper pentru a calcula totalul alertelor
    private int calculateTotalAlerts(List<Car> cars) {
        int alertsCount = 0;
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();

        for (Car car : cars) {
            if (car.getItpExpirationDate() != null && car.getItpExpirationDate().before(today)) {
                alertsCount++;
            }
            if (car.getLastOilChange() != null && car.getLastOilChange().before(oneYearAgo)) {
                alertsCount++;
            }
            if (car.getInsuranceExpirationDate() != null && car.getInsuranceExpirationDate().before(today)) {
                alertsCount++;
            }
        }
        return alertsCount;
    }
}
