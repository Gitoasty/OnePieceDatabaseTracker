package pwa.project.one_piece.views.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.service.CharacterService;

@Controller
@RequestMapping("/admin-characters")
public class AdminCharacterViewController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("characterList", characterService.getAllCharacters());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        return "admin-characters";
    }

    @GetMapping("/with-fruits")
    public String showFruitUsers(Model model) {
        model.addAttribute("characterList", characterService.findWithFruit());
        model.addAttribute("fruitFilter", true);
        model.addAttribute("noFruitFilter", false);
        return "admin-characters";
    }

    @GetMapping("/without-fruits")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("characterList", characterService.findWithoutFruit());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", true);
        return "admin-characters";
    }
}
