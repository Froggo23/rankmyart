package ian.choe.rankmyart.domain.user.service;
import ian.choe.rankmyart.domain.user.repository.UserRepository;
import ian.choe.rankmyart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ian.choe.rankmyart.domain.artwork.service.S3FileService; // Import S3FileService
import java.io.IOException;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3FileService s3FileService; // Inject S3FileService
    @Autowired
    public UserService(UserRepository userRepository, S3FileService s3FileService) {
        this.userRepository = userRepository;
        this.s3FileService = s3FileService;
    }
    /**
     * Registers a new user. Sets a default profile image and an empty bio.
     * @param user The User object containing registration details.
     * @return true if registration is successful, false if username or email already exists.
     */
    public boolean registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false; // Username already exists
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false; // Email already exists
        }
        // Set default profile image and bio for new users
        user.setProfileImg("/img/default-avatar.svg"); // Default avatar URL
        user.setBio(""); // Empty bio
        userRepository.save(user);
        return true;
    }
    /**
     * Authenticates a user based on username and raw password.
     * @param username The username to authenticate.
     * @param rawPassword The raw password provided by the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // In a real application, use a strong password encoder (e.g., BCrypt)
            return user.getPassword().equals(rawPassword);
        }
        return false;
    }
    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    /**
     * Finds or creates a user via Google SSO.
     * If a user with the given Google ID exists, returns that user.
     * Otherwise, creates a new user with Google credentials.
     * @param googleId The Google user ID.
     * @param email The user's email from Google.
     * @param username The username (derived from email or Google profile).
     * @return The User object (existing or newly created).
     */
    public User findOrCreateGoogleUser(String googleId, String email, String username) {
        // Check if user exists by Google ID
        User user = userRepository.findByGoogleId(googleId);
        if (user != null) {
            return user; // User already exists
        }
        
        // Check if user exists by email (for linking existing accounts)
        user = userRepository.findByEmail(email);
        if (user != null) {
            // Link Google ID to existing account (would need an update method in repository)
            // For now, we'll just return the existing user
            return user;
        }
        
        // Create new user with Google credentials
        user = new User();
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(null); // No password for Google SSO users
        user.setProfileImg("/img/default-avatar.svg"); // Default avatar
        user.setBio(""); // Empty bio
        
        userRepository.save(user);
        return user;
    }
    /**
     * Updates a user's profile, including their profile image and bio.
     * If a new profile image is provided, the old one (if not default) is deleted from S3.
     * @param username The username of the user whose profile is being updated.
     * @param profileImage The new profile image file (can be null if not changing).
     * @param bio The new bio text.
     * @return true if the profile is updated successfully, false otherwise.
     */
    public boolean updateProfile(String originalUsername, String newUsername, MultipartFile profileImage, String bio) {
        User user = userRepository.findByUsername(originalUsername);
        if (user == null) {
            return false; // User not found
        }
        String newProfileImgUrl = user.getProfileImg();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                if (newProfileImgUrl != null) {
                    s3FileService.deleteFile(newProfileImgUrl);
                }
                newProfileImgUrl = s3FileService.uploadFile(profileImage);
            } catch (IOException e) {
                return false;
            }
        }
        userRepository.updateUserProfile(originalUsername, newUsername, newProfileImgUrl, bio);
        return true;
    }
}
