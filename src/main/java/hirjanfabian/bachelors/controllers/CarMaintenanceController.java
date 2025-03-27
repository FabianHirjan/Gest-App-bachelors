package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car/maintenance")
public class CarMaintenanceController {

    private final CarService carMaintenanceService;

    @PostMapping("/{carId}/insurance")
    public ResponseEntity<String> markInsurance(@PathVariable Long carId) {
        carMaintenanceService.markInsurance(carId);
        return ResponseEntity.ok("Insurance marked successfully.");
    }

    @PostMapping("/{carId}/oil-change")
    public ResponseEntity<String> markOilChange(@PathVariable Long carId) {
        carMaintenanceService.markOilChange(carId);
        return ResponseEntity.ok("Oil change marked successfully.");
    }

    @PostMapping("/{carId}/tire-change")
    public ResponseEntity<String> markTireChange(@PathVariable Long carId) {
        carMaintenanceService.markTireChange(carId);
        return ResponseEntity.ok("Tire change marked successfully.");
    }

    @PostMapping("/{carId}/maintenance")
    public ResponseEntity<String> markMaintenance(@PathVariable Long carId) {
        carMaintenanceService.markMaintenance(carId);
        return ResponseEntity.ok("Maintenance marked successfully.");
    }
}
