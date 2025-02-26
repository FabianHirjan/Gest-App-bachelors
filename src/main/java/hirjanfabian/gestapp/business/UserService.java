package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public Boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Car getCarForUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            return user.getCar();
        }
        return null;
    }

}
