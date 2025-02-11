package hirjanfabian.gestapp.repositories;

import hirjanfabian.gestapp.entities.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Logs, Long> {
}
