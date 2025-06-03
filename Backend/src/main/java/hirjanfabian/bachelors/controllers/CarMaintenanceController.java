package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * REST controller exposing endpoints for marking maintenance events on a {@link Car}.
 *
 * <p>All operations are idempotent and return <em>200 OK</em> on success. A negative or {@code null} identifier
 * yields <em>400 Bad Request</em>. Any server-side failure is logged and results in
 * <em>500 Internal Server Error</em>.</p>
 *
 * <p>The underlying {@link CarService} methods are asynchronous, keeping these endpoints non-blocking.</p>
 */
@RestController
@RequestMapping("/api/car/maintenance")
@RequiredArgsConstructor
public class CarMaintenanceController {

    private final CarService carMaintenanceService;

    private static ResponseEntity<String> invalidId() {
        return ResponseEntity.badRequest().body("carId must be positive");
    }

    @PostMapping("/{carId}/insurance")
    public ResponseEntity<String> markInsurance(@PathVariable Long carId) {
        if (carId == null || carId <= 0) return invalidId();
        try {
            carMaintenanceService.markInsurance(carId);
            return ResponseEntity.ok("Insurance marked successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }

    @PostMapping("/{carId}/oil-change")
    public ResponseEntity<String> markOilChange(@PathVariable Long carId) {
        if (carId == null || carId <= 0) return invalidId();
        try {
            carMaintenanceService.markOilChange(carId);
            return ResponseEntity.ok("Oil change marked successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }

    @PostMapping("/{carId}/tire-change")
    public ResponseEntity<String> markTireChange(@PathVariable Long carId) {
        if (carId == null || carId <= 0) return invalidId();
        try {
            carMaintenanceService.markTireChange(carId);
            return ResponseEntity.ok("Tire change marked successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }

    @PostMapping("/{carId}/maintenance")
    public ResponseEntity<String> markMaintenance(@PathVariable Long carId) {
        if (carId == null || carId <= 0) return invalidId();
        try {
            carMaintenanceService.markMaintenance(carId);
            return ResponseEntity.ok("Maintenance marked successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }
}
