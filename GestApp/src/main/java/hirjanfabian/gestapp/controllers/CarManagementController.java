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

@Controller
@RequestMapping("/cars")
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

    // Configurăm conversia pentru câmpurile de tip Date
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/view")
    public String showCarManagementPage(Model model) {
        List<Car> cars = carService.getAllCars();
        List<User> drivers = userService.getAllUsers();
        int totalAlerts = calculateTotalAlerts(cars);

        // Astăzi, pentru comparații
        Date today = new Date();

        // Pentru a compara cu data de peste un an în urmă
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();

        // Pentru formularul de creare a unei mașini noi
        model.addAttribute("car", new Car());
        model.addAttribute("cars", cars);
        model.addAttribute("drivers", drivers);
        model.addAttribute("totalAlerts", totalAlerts);
        model.addAttribute("today", today);
        model.addAttribute("oneYearAgo", oneYearAgo);

        return "carManagement"; // numele template-ului Thymeleaf
    }

    // CREATE Car
    @PostMapping
    public String createCar(@ModelAttribute("car") Car car,
                            RedirectAttributes ra) {
        Car createdCar = carService.createCar(car);
        if (createdCar != null) {
            ra.addFlashAttribute("message", "Car created successfully!");
        } else {
            ra.addFlashAttribute("error", "Error creating car.");
        }
        return "redirect:/cars/view";
    }

    // UPDATE Car
    @PostMapping("/update")
    public String updateCar(@ModelAttribute Car car, RedirectAttributes ra) {
        // Căutăm mașina existentă în baza de date
        Car existingCar = carService.findById(car.getId());
        if (existingCar == null) {
            ra.addFlashAttribute("error", "Car not found for update.");
            return "redirect:/cars/view";
        }

        // Actualizăm câmpurile
        existingCar.setType(car.getType());
        existingCar.setLicensePlate(car.getLicensePlate());
        existingCar.setMileage(car.getMileage());
        existingCar.setLastOilChange(car.getLastOilChange());
        existingCar.setInsuranceExpirationDate(car.getInsuranceExpirationDate());
        existingCar.setItpExpirationDate(car.getItpExpirationDate());

        // Persistăm modificările (folosind metoda createCar sau save, după implementare)
        Car savedCar = carService.createCar(existingCar);

        if (savedCar != null) {
            ra.addFlashAttribute("message", "Car updated successfully!");
        } else {
            ra.addFlashAttribute("error", "Error updating car.");
        }
        return "redirect:/cars/view";
    }

    // DELETE Car
    @PostMapping("/delete")
    public String deleteCar(@RequestParam("id") Long id, RedirectAttributes ra) {
        boolean deleted = carService.deleteCar(id);
        if (deleted) {
            ra.addFlashAttribute("message", "Car deleted successfully!");
        } else {
            ra.addFlashAttribute("error", "Error deleting car.");
        }
        return "redirect:/cars/view";
    }

    // ASSIGN driver
    @PostMapping("/assign")
    public String assignDriver(@RequestParam("carID") Long carID,
                               @RequestParam("driverID") Long driverID,
                               RedirectAttributes ra) {

        Car car = carService.findById(carID);
        User driver = userService.getUserById(driverID);

        if (car == null || driver == null) {
            ra.addFlashAttribute("error", "Car or Driver not found.");
            return "redirect:/cars/view";
        }

        Car updatedCar = assignDriverToCarService.assignCarToDriver(car, driver);
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
        Car car = carService.findById(carID);
        if (car == null) {
            ra.addFlashAttribute("error", "Car not found.");
            return "redirect:/cars/view";
        }

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
