package pwa.project.one_piece.views.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.service.CharacterService;
import pwa.project.one_piece.entity.Character;

import java.io.IOException;
import java.util.Optional;

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

    @GetMapping("/byName/{name}")
    public String showByName(Model model, @PathVariable String name) {
        model.addAttribute("character", new Character());
        model.addAttribute("characterList", characterService.getCharactersByName(name));
        model.addAttribute("fruitFilter", false);
        model.addAttribute("noFruitFilter", false);
        model.addAttribute("searchName", name);
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
            @RequestParam String option,
            @RequestParam(required = false) String searchName
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

                    characterService.save(character);
                }
                break;
            case "Patch":
                Optional<Character> character = characterService.getCharacterByName(name);
                if (character.isEmpty()) {
                    return "redirect:/admin-characters";
                }

                if (name != null && !name.isEmpty()) {
                    character.get().setName(name);
                } else {
                    return "redirect:/admin-characters";
                }
                if (chapterIntroduced != null && !chapterIntroduced.isEmpty()) {
                    character.get().setChapterIntroduced(chapterIntroduced);
                }
                if (episodeIntroduced != null && !episodeIntroduced.isEmpty()) {
                    character.get().setEpisodeIntroduced(episodeIntroduced);
                }
                if (yearIntroduced != null && !yearIntroduced.isEmpty() && yearIntroduced.matches("^[1-9]\\d*$")) {
                    character.get().setYearIntroduced(yearIntroduced);
                }
                if (note != null && !note.isEmpty()) {
                    character.get().setNote(note);
                }

                characterService.save(character.get());
                break;
            case "Delete":
                characterService.delete(name);
                break;
            case "Scrape":
                scrape();
                break;
            case "FindWithName":
                if (name != null && !name.isEmpty()) {
                    return "redirect:/admin-characters/byName/" + name;
                }
            default:
                break;
        }

        // If we have a search context, redirect back to the filtered view
        if (searchName != null && !searchName.isEmpty()) {
            return "redirect:/admin-characters/byName/" + searchName;
        }

        return "redirect:/admin-characters";
    }
}
