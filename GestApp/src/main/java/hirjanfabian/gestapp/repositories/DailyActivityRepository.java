package hirjanfabian.gestapp.repositories;

import hirjanfabian.gestapp.entities.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
}
