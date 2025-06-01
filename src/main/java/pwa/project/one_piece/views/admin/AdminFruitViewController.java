package pwa.project.one_piece.views.admin;

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
 * <h1>
 *     Controller class for admin fruits page
 * </h1>
 */
@Controller
@RequestMapping("/admin-fruits")
public class AdminFruitViewController {

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
        return "admin-fruits";
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
    public String showFruitsWithUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithUsers());
        model.addAttribute("userFilter", true);
        model.addAttribute("noUserFilter", false);
        return "admin-fruits";
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
    public String showFruitsWithoutUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithoutUsers());
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", true);
        return "admin-fruits";
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
        return "admin-fruits";
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
     * @param power {@link String} power of devil fruit
     * @param awakened {@link String} awakened power of devil fruit
     * @param weakness {@link String} weakness of devil fruit
     * @param fruitType {@link FruitType} type of devil fruit
     * @param character {@link String} name of character
     * @param option {@link String} action to be performed
     * @param searchName {@link String} stored search name
     * @return {@link String} page to be displayed
     */
    @PostMapping("/save")
    public String addFruit(
            @RequestParam String name,
            @RequestParam String power,
            @RequestParam(required = false) String awakened,
            @RequestParam(required = false) String weakness,
            @RequestParam FruitType fruitType,
            @RequestParam(required = false) String character,
            @RequestParam String option,
            @RequestParam(required = false) String searchName
    ) {
        switch (option) {
            case "Add":
                if (!name.isEmpty() && !power.isEmpty() && fruitType != null) {
                    // No longer requiring awakened to be non-empty
                    fruitService.addFruit(name, power, awakened, weakness, fruitType, character);
                }
                break;
            case "Patch":
                List<Fruit> fruitList = fruitService.getFruitByName(name);
                if (fruitList.isEmpty()) {
                    return "redirect:/admin-fruits";
                }

                Fruit fruit = fruitList.getFirst();

                if (name == null || name.isEmpty()) {
                    return "redirect:/admin-fruits";
                }

                fruit.setName(name);

                if (power != null && !power.isEmpty()) {
                    fruit.setPower(power);
                }

                fruit.setAwakened(awakened);
                fruit.setWeakness(weakness);

                if (fruitType != null) {
                    fruit.setType(fruitType);
                }

                if (character != null) {
                    if (character.equals("reset")) {
                        if (fruit.getCharacter() != null) {
                            Character oldCharacter = fruit.getCharacter();
                            oldCharacter.getFruits().remove(fruit);
                            fruit.setCharacter(null);
                        }
                    } else if (!character.trim().isEmpty()) {
                        fruitService.updateFruitCharacter(fruit, character);
                    }
                }

                fruitService.update(fruit);
                break;
            case "Delete":
                fruitService.delete(name);
                break;
            case "FindWithName":
                if (name != null && !name.isEmpty()) {
                    return "redirect:/admin-fruits/byName/" + name;
                }
            default:
                break;
        }

        if (searchName != null && !searchName.isEmpty()) {
            return "redirect:/admin-fruits/byName/" + searchName;
        }

        return "redirect:/admin-fruits";
    }
}
