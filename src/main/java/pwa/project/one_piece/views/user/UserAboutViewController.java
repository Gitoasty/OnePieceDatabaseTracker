package pwa.project.one_piece.views.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user-about")
public class UserAboutViewController {

    @GetMapping
    public String showCharactersPage(Model model) {
        return "user-about";
    }
}
