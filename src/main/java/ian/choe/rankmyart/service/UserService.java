package ian.choe.rankmyart.service;

import ian.choe.rankmyart.model.User;
import ian.choe.rankmyart.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerNewUser(User user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // WARNING: Saving plain text password. HIGH SECURITY RISK.
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // WARNING: Comparing plain text passwords. HIGH SECURITY RISK.
            return user.getPassword().equals(rawPassword);
        }
        return false;
    }
}