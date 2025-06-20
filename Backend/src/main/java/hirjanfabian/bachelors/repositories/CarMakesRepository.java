package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.CarMakes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarMakesRepository extends JpaRepository<CarMakes, Long>{
    CarMakes findByMake(String make);
    CarMakes findById(long id);
    @EntityGraph(attributePaths = {"models"})
    List<CarMakes> findAll();

    @EntityGraph(attributePaths = "models")          // <-- eager load models
    @Query("select cm from CarMakes cm")             // <-- plain JPQL
    List<CarMakes> findAllWithModels();

}
