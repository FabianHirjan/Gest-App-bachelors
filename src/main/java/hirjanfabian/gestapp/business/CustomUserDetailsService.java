package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    /**
     * Retrieves the {@link UserDetails} of a user based on the provided username.
     * The method queries the user repository to fetch the user details and constructs
     * a {@link User} object with the retrieved data.
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} implementation containing the user's information
     * @throws UsernameNotFoundException if no user is found with the provided username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        hirjanfabian.gestapp.entities.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
