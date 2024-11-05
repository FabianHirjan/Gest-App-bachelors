package hirjanfabian.gestapp.repositories;

import hirjanfabian.gestapp.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Long, Car> {
}
