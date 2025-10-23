package ian.choe.rankmyart.domain.user.controller;
import ian.choe.rankmyart.domain.user.service.UserService;
import ian.choe.rankmyart.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import java.util.Map;

@RestController
public class UserRestController {
    public final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User getUser(HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return null;
        }
        String username = loginCookie.getValue();
        return userService.findByUsername(username);
    }

    /**
     * Endpoint to handle Google OAuth login.
     * Receives Google user information, creates or retrieves user, and sets login cookie.
     * @param googleUserInfo Map containing googleId, email, and name from Google OAuth
     * @param response HttpServletResponse to set cookies
     * @return ResponseEntity with User object or error status
     */
    @PostMapping("/auth/google")
    public ResponseEntity<User> googleLogin(@RequestBody Map<String, String> googleUserInfo, HttpServletResponse response) {
        try {
            String googleId = googleUserInfo.get("googleId");
            String email = googleUserInfo.get("email");
            String name = googleUserInfo.get("name");

            if (googleId == null || email == null) {
                return ResponseEntity.badRequest().build();
            }

            // Generate username from email or name
            String username = name != null ? name.replaceAll("\\s+", "").toLowerCase() : email.split("@")[0];
            
            // Find or create user
            User user = userService.findOrCreateGoogleUser(googleId, email, username);

            // Set login cookie
            Cookie loginCookie = new Cookie("username", user.getUsername());
            loginCookie.setPath("/");
            loginCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(loginCookie);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
