package hirjanfabian.bachelors.respositories;

import hirjanfabian.bachelors.entities.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
}
