package pwa.project.one_piece.views.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.service.CharacterService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user-characters")
public class UserCharacterViewController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getAllCharacters());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        return "user-characters";
    }

    @GetMapping("/with-fruits")
    public String showFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithFruit());
        model.addAttribute("fruitFilter", true);
        model.addAttribute("noFruitFilter", false);
        return "user-characters";
    }

    @GetMapping("/without-fruits")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithoutFruit());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", true);
        return "user-characters";
    }

    @GetMapping("/byName/{name}")
    public String showByName(Model model, @PathVariable String name) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getCharactersByName(name));
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        model.addAttribute("searchName", name);
        return "user-characters";
    }
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
