package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
}
