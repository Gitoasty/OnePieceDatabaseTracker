package pwa.project.one_piece.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.service.CharacterService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    Logger logger = LoggerFactory.getLogger(CharacterController.class);

    @PostMapping("/save")
    public ResponseEntity<Character> save(@RequestBody Character character) {
        characterService.save(character);
        logger.info("Character saved successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(character);
    }

    @GetMapping("/all")
    public List<Character> findAll() {
        logger.info("Find all characters");
        return characterService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Character> findStudentsOld(@PathVariable Integer id) {
        logger.info("Find characters with id: " + id);
        return characterService.getUserById(id);
    }


    @GetMapping("/{name}")
    public List<Character> findAllWithName(@RequestParam String name) {
        return characterService.searchCharactersByName(name);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        logger.info("Deleted character with id: " + id);
        characterService.delete(id);
    }
}
