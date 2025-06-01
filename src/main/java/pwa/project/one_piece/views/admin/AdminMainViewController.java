package pwa.project.one_piece.views.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>
 *     Controller class for admin main
 * </h1>
 */
@Controller
@RequestMapping("/admin-main")
public class AdminMainViewController {

    /**
     * <h2>
     *     Method for showing the page
     * </h2>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping
    public String showCharactersPage(Model model) {
        return "admin-main";
    }
}
