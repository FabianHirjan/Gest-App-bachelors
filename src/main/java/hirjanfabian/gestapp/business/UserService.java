package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    /**
     * Repository interface for performing CRUD operations and custom queries
     * related to User entities.
     * This is used within the UserService for managing user-related data access.
     */
    private final UserRepository userRepository;

    /**
     * Constructs an instance of UserService.
     *
     * @param userRepository the repository for managing User entities
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all the users from the repository.
     *
     * @return a list of all User entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be retrieved
     * @return the User object if found, or null if no user exists with the given id
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a user by their username from the user repository.
     * If no user is found, returns null.
     *
     * @param username the username of the user to be retrieved
     * @return the User object if found, otherwise null
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @return true if the user was successfully deleted, false if the user does not exist
     */
    public Boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the car associated with a specific user, identified by their user ID.
     *
     * @param id the unique identifier of the user whose car needs to be retrieved
     * @return the car associated with the user, or null if the user does not exist
     * or does not have a car
     */
    public Car getCarForUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            return user.getCar();
        }
        return null;
    }

}
