package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.model.User;
import ian.choe.rankmyart.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

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
}