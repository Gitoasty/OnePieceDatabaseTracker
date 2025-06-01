package pwa.project.one_piece.views.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.entity.Fruit;
import pwa.project.one_piece.entity.FruitType;
import pwa.project.one_piece.service.CharacterService;
import pwa.project.one_piece.service.FruitService;

import java.util.List;

/**
 * Controller class for user fruits
 */
@Controller
@RequestMapping("/user-fruits")
public class UserFruitViewController {

    @Autowired
    private FruitService fruitService;
    @Autowired
    private CharacterService characterService;

    /**
     * <h2>
     *     Method for showing the page
     * </h2>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("fruitList", fruitService.getAllFruits());
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", false);
        return "user-fruits";
    }

    /**
     * <h2>
     *     Method for filtered fruits
     * </h2>
     * <p>
     *     Displays fruits which have users.
     * </p>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping("/with-user")
    public String showFruitUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithUsers());
        model.addAttribute("userFilter", true);
        model.addAttribute("noUserFilter", false);
        return "user-fruits";
    }

    /**
     * <h2>
     *     Method for filtered fruits
     * </h2>
     * <p>
     *     Displays fruits which don't have users.
     * </p>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping("/without-users")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithoutUsers());
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", true);
        return "user-fruits";
    }

    /**
     * <h2>
     *     Method for displaying filtered page
     * </h2>
     * <p>
     *     Filters fruits whose names contain given String
     * </p>
     * @param model {@link Model} from Spring framework
     * @param name {@link String} substring of fruit name
     * @return {@link String} page to be displayed
     */
    @GetMapping("/byName/{name}")
    public String showByName(Model model, @PathVariable String name) {
        model.addAttribute("fruit", new Fruit());
        model.addAttribute("fruitList", fruitService.getFruitByName(name));
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", true);
        model.addAttribute("searchName", name);
        return "user-fruits";
    }

    /**
     * <h2>
     *     Method for performing fruit actions
     * </h2>
     * <p>
     *     Method receives parameters by which to create fruit and perform selected action.
     *     Action depends on the option parameter passed.
     * </p>
     * @param name {@link String} name of devil fruit
     * @param searchName {@link String} stored search name
     * @return {@link String} page to be displayed
     */
    @PostMapping("/save")
    public String addFruit(
            @RequestParam String name,
            @RequestParam(required = false) String searchName
    ) {
        if (name != null && !name.isEmpty()) {
            return "redirect:/user-fruits/byName/" + name;
        }

        if (searchName != null && !searchName.isEmpty()) {
            return "redirect:/user-fruits/byName/" + searchName;
        }

        return "redirect:/user-fruits";
    }
}
