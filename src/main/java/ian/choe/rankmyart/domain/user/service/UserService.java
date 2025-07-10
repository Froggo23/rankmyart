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
        user.setProfileImg("https://i.pravatar.cc/150"); // Default avatar URL
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
     * Updates a user's profile, including their profile image and bio.
     * If a new profile image is provided, the old one (if not default) is deleted from S3.
     * @param username The username of the user whose profile is being updated.
     * @param profileImage The new profile image file (can be null if not changing).
     * @param bio The new bio text.
     * @return true if the profile is updated successfully, false otherwise.
     */
    public boolean updateProfile(String username, MultipartFile profileImage, String bio) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.err.println("User not found for username: " + username);
            return false; // User not found
        }

        String newProfileImgUrl = user.getProfileImg(); // Start with current image URL
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // Delete old profile image from S3 if it's not the default one
                // and if a new image is actually being uploaded.
                if (newProfileImgUrl != null) {
                    s3FileService.deleteFile(newProfileImgUrl);
                }
                newProfileImgUrl = s3FileService.uploadFile(profileImage);
            } catch (IOException e) {
                System.err.println("Error uploading profile image for user " + username + ": " + e.getMessage());
                return false; // Failed to upload image
            }
        }

        // Update the user's profile in the database
        userRepository.updateUserProfile(username, newProfileImgUrl, bio);
        return true;
    }
}
