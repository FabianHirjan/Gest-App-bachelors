package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UIService {
    private final UserRepository userRepository;

    public UIService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}