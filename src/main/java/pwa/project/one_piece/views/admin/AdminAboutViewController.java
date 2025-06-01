package pwa.project.one_piece.views.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.entity.ContactForm;

/**
 * <h1>
 *     Controller class for the admin about page
 * </h1>
 */
@Controller
@RequestMapping("/admin-about")
public class AdminAboutViewController {

    /**
     * <h2>
     *     Method for showing the page
     * </h2>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping
    public String showCharactersPage(Model model) {
        return "admin-about";
    }
}
