package ian.choe.rankmyart.domain.user.controller;

import ian.choe.rankmyart.model.User;
import ian.choe.rankmyart.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        boolean registered = userService.registerNewUser(newUser);

        if (registered) {
            return ResponseEntity.ok("Registration successful!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists.");
        }
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        boolean authenticated = userService.authenticateUser(username, password);

        if (authenticated) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    @GetMapping("/editprofile")
    public String showEditProfilePage() {
        return "editprofile";
    }

    /**
     * Handles requests to update a user's profile.
     * Allows updating the profile image and bio.
     * Redirects to the profile page on success.
     * @param avatarFile The new profile image file (optional).
     * @param bio The new bio text.
     * @param request HttpServletRequest to get the username from cookies.
     * @return ResponseEntity indicating success or failure, with redirection on success.
     */
    @PostMapping("/update-profile")
    public ResponseEntity<String> updateProfile(
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam("bio") String bio,
            HttpServletRequest request) {

        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        String username = loginCookie.getValue();

        boolean updated = userService.updateProfile(username, avatarFile, bio);

        if (updated) {
            // Redirect to the profile page after successful update
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/profile").build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile.");
        }
    }
}
