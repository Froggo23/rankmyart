package ian.choe.rankmyart.domain.user.controller;

import ian.choe.rankmyart.domain.user.service.UserService;
import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

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
}
