package hirjanfabian.bachelors.respositories;

import hirjanfabian.bachelors.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
