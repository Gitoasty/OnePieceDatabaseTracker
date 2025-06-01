package pwa.project.one_piece.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>
 *     Controller class for login page
 * </h1>
 */
@Controller
@RequestMapping("/login")
public class LoginViewController {

    /**
     * <h2>
     *     Method for showing the page
     * </h2>
     * @return {@link String} page to be displayed
     */
    @GetMapping()
    public String showLoginForm() {
        return "login";
    }
}
