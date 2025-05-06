package pwa.project.one_piece.views.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.service.CharacterService;
import pwa.project.one_piece.entity.Character;

@Controller
@RequestMapping("/admin-characters")
public class AdminCharacterViewController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getAllCharacters());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        return "admin-characters";
    }

    @GetMapping("/with-fruits")
    public String showFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithFruit());
        model.addAttribute("fruitFilter", true);
        model.addAttribute("noFruitFilter", false);
        return "admin-characters";
    }

    @GetMapping("/without-fruits")
    public String showNonFruitUsers(Model model) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.findWithoutFruit());
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", true);
        return "admin-characters";
    }

    @PostMapping("/submit")
    public String createCharacter(
            @RequestParam String name,
            @RequestParam(required = false) String chapterIntroduced,
            @RequestParam(required = false) String episodeIntroduced,
            @RequestParam String yearIntroduced,
            @RequestParam(required = false) String note
    ) {
        Character character = new Character();
        character.setName(name);
        character.setChapterIntroduced(chapterIntroduced);
        character.setEpisodeIntroduced(episodeIntroduced);
        character.setYearIntroduced(yearIntroduced);
        character.setNote(note);

        character.setMaster(name.contains("Marshall D."));

        characterService.save(character);
        return "redirect:/admin-characters";
    }
}
