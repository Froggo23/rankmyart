package ian.choe.rankmyart.domain.user.service;

import ian.choe.rankmyart.model.User;
import ian.choe.rankmyart.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getPassword().equals(rawPassword);
        }
        return false;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}