package pwa.project.one_piece.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-main")
public class AdminMainViewController {

    @GetMapping
    public String showCharactersPage(Model model) {
        return "admin-main";
    }
}
