package pwa.project.one_piece.views.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.service.CharacterService;
import pwa.project.one_piece.entity.Character;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("a");
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

    public void scrape() {
        try {
            characterService.scrapeAndSaveCharacters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/save")
    public String manipulateCharacter(
            @RequestParam String name,
            @RequestParam(required = false) String chapterIntroduced,
            @RequestParam(required = false) String episodeIntroduced,
            @RequestParam String yearIntroduced,
            @RequestParam(required = false) String note,
            @RequestParam String option
    ) {

        switch (option) {
            case "Add":
                if (!name.isEmpty() && !yearIntroduced.isEmpty() && yearIntroduced.matches("^[1-9]\\d*$")) {
                    Character character = new Character();
                    character.setName(name);
                    character.setChapterIntroduced(chapterIntroduced);
                    character.setEpisodeIntroduced(episodeIntroduced);
                    character.setYearIntroduced(yearIntroduced);
                    character.setNote(note);

                    character.setMaster(name.contains("Marshall D."));
                    characterService.save(character);
                }
                break;
            case "Patch":
                Character character = characterService.getCharacterByName(name);
                if (character == null) {
                    return "redirect:/admin-characters";
                }

                if (name != null && !name.isEmpty()) {
                    character.setName(name);
                } else {
                    return "redirect:/admin-characters";
                }
                if (chapterIntroduced != null && !chapterIntroduced.isEmpty()) {
                    character.setChapterIntroduced(chapterIntroduced);
                }
                if (episodeIntroduced != null && !episodeIntroduced.isEmpty()) {
                    character.setEpisodeIntroduced(episodeIntroduced);
                }
                if (yearIntroduced != null && !yearIntroduced.isEmpty() && yearIntroduced.matches("^[1-9]\\d*$")) {
                    character.setYearIntroduced(yearIntroduced);
                }
                if (note != null && !note.isEmpty()) {
                    character.setNote(note);
                }

                characterService.save(character);
                break;
            case "Delete":
                characterService.delete(name);
                break;
            case "Scrape":
                scrape();
                break;
            default:
                break;
        }

        return "redirect:/admin-characters";
    }
}
