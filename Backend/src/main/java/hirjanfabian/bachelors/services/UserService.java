package hirjanfabian.bachelors.services;


import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.UserRepository;
import hirjanfabian.bachelors.utils.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public boolean loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && PasswordUtil.checkPassword(password, user.getPassword());
    }


    public List<User> findAllExcept(String username) {

        List<User> response =  userRepository.findByUsernameNot(username);
        response.forEach(user -> user.setPassword(null));
        return response;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
