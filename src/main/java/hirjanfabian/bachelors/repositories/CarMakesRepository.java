package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.CarMakes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarMakesRepository extends JpaRepository<CarMakes, Long>{
    CarMakes findByMake(String make);
    CarMakes findById(long id);
}
