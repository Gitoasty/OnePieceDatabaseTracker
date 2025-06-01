package pwa.project.one_piece.views.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.entity.FruitType;
import pwa.project.one_piece.service.CharacterService;

import java.io.IOException;
import java.util.Optional;

/**
 * <h1>
 *     Controller class for user characters
 * </h1>
 */
@Controller
@RequestMapping("/user-characters")
public class UserCharacterViewController {

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
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getAllCharacters());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        return "user-characters";
    }

    /**
     * <h2>
     *     Method for displaying filtered page
     * </h2>
     * <p>
     *     Filters characters that have devil fruits
     * </p>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping("/with-fruits")
    public String showFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithFruit());
        model.addAttribute("fruitFilter", true);
        model.addAttribute("noFruitFilter", false);
        return "user-characters";
    }

    /**
     * <h2>
     *     Method for displaying filtered page
     * </h2>
     * <p>
     *     Filters characters that don't have devil fruits
     * </p>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping("/without-fruits")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithoutFruit());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", true);
        return "user-characters";
    }

    /**
     * <h2>
     *     Method for displaying filtered page
     * </h2>
     * <p>
     *     Filters characters whose names contain given String
     * </p>
     * @param model {@link Model} from Spring framework
     * @param name {@link String} substring of character name
     * @return {@link String} page to be displayed
     */
    @GetMapping("/byName/{name}")
    public String showByName(Model model, @PathVariable String name) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getCharactersByName(name));
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        model.addAttribute("searchName", name);
        return "user-characters";
    }

    /**
     * <h2>
     *     Method for performing character actions
     * </h2>
     * <p>
     *     Performs the search operation
     * </p>
     * @param name {@link String} name of devil fruit
     * @param searchName {@link String} stored search name
     * @return {@link String} page to be displayed
     */
    @PostMapping("/save")
    public String manipulateCharacter(
            @RequestParam String name,
            @RequestParam(required = false) String searchName
    ) {
        if (name != null && !name.isEmpty()) {
            return "redirect:/user-characters/byName/" + name;
        }

        if (searchName != null && !searchName.isEmpty()) {
            return "redirect:/user-characters/byName/" + searchName;
        }

        return "redirect:/user-characters";
    }
}
