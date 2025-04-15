package pwa.project.one_piece.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginViewController {

    @GetMapping()
    public String showLoginForm() {
        return "login";
    }
}
