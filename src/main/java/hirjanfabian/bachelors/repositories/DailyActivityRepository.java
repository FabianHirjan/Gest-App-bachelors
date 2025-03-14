package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    DailyActivity findByUserId(Long userId);
}