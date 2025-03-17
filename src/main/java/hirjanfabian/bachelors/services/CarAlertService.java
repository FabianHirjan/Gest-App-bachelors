package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CarAlertService {

    public Map<String, String> getCarAlerts(Car car) {
        Map<String, String> alerts = new HashMap<>();

        int oilChangeInterval = 50000;
        if (car.getMileage() > oilChangeInterval) {
            alerts.put("oilChange", "Oil change is overdue by " + (car.getMileage() - oilChangeInterval) + " km");
        }

        if (car.getInsuranceExpiration() != null && car.getInsuranceExpiration().isBefore(LocalDateTime.now())) {
            alerts.put("insuranceExpiration", "Insurance has expired on " + car.getInsuranceExpiration());
        }

        if (car.getLastInspection() != null && car.getLastInspection().isBefore(LocalDateTime.now().minusYears(1))) {
            alerts.put("lastInspection", "Last inspection was over a year ago on " + car.getLastInspection());
        }

        if (car.getLastTireChange() != null && car.getLastTireChange().isBefore(LocalDateTime.now().minusYears(1))) {
            alerts.put("lastTireChange", "Last tire change was over a year ago on " + car.getLastTireChange());
        }

        if (car.getLastOilChange() != null && car.getLastOilChange().isBefore(LocalDateTime.now().minusYears(1))) {
            alerts.put("lastOilChange", "Last oil change was over a year ago on " + car.getLastOilChange());
        }

        return alerts;
    }
}