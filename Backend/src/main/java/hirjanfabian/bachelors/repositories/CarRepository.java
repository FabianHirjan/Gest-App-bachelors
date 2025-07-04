package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.concurrent.CompletionStage;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByLicensePlate(String licensePlate);
}
