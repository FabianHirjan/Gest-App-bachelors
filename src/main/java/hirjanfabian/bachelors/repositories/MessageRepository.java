package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
