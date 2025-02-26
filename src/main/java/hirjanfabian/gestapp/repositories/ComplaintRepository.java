package hirjanfabian.gestapp.repositories;

import hirjanfabian.gestapp.entities.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaints, Long> {
}
