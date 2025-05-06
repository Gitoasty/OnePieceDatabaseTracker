package pwa.project.one_piece.views.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.entity.Fruit;
import pwa.project.one_piece.service.CharacterService;
import pwa.project.one_piece.service.FruitService;

@Controller
@RequestMapping("/admin-fruits")
public class AdminFruitViewController {

    @Autowired
    private FruitService fruitService;

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

    @PostMapping("/submitForm")
    public String handleFormSubmission(@ModelAttribute Fruit fruit) {
        fruitService.save(fruit);
        return "admin-fruits";
    }
}
