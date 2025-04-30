package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.CarModels;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelsRepository extends JpaRepository<CarModels, Long> {


    @Query("""
           select m
           from CarModels m
           left join fetch m.carMake
           """)
    List<CarModels> findAllWithMake();
}
