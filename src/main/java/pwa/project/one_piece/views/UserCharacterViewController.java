package pwa.project.one_piece.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.service.CharacterService;

@Controller
@RequestMapping("/characters")
public class UserCharacterViewController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("characterList", characterService.getAllCharacters());
        return "characters";
    }
}
