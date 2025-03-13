package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
