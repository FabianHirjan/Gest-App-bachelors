package hirjanfabian.bachelors.respositories;

import hirjanfabian.bachelors.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
    List<User> findByUsernameNot(String username);
}
