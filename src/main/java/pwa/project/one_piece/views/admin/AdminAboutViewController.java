package pwa.project.one_piece.views.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.entity.ContactForm;

@Controller
@RequestMapping("admin-about")
public class AdminAboutViewController {

    @GetMapping
    public String showCharactersPage(Model model) {
        return "admin-about";
    }
}
