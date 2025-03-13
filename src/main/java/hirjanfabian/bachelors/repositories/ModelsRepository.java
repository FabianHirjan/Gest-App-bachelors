package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.CarModels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelsRepository extends JpaRepository<CarModels, Long> {
}
