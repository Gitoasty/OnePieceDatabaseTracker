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

@Controller
@RequestMapping("/admin-fruits")
public class AdminFruitViewController {

    @Autowired
    private FruitService fruitService;
    @Autowired
    private CharacterService characterService;

    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("fruitList", fruitService.getAllFruits());
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", false);
        return "admin-fruits";
    }

    @GetMapping("/with-user")
    public String showFruitUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithUsers());
        model.addAttribute("userFilter", true);
        model.addAttribute("noUserFilter", false);
        return "admin-fruits";
    }

    @GetMapping("/without-users")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("fruitList", fruitService.getFruitsWithoutUsers());
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", true);
        return "admin-fruits";
    }

    @GetMapping("/byName/{name}")
    public String showByName(Model model, @PathVariable String name) {
        model.addAttribute("fruit", new Fruit());
        model.addAttribute("fruitList", fruitService.getFruitByName(name));
        model.addAttribute("userFilter", false);
        model.addAttribute("noUserFilter", true);
        model.addAttribute("searchName", name);
        return "admin-fruits";
    }

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
