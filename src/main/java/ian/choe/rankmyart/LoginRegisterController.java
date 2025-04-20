package ian.choe.rankmyart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRegisterController {
    @GetMapping("/signup")
    public String login() {
        return "signup";
    }

    @GetMapping("/upload")
    public String register() {
        return "upload";
    }


}
