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
public class CharacterRestController {

    @Autowired
    private CharacterService characterService;

    Logger logger = LoggerFactory.getLogger(CharacterRestController.class);

    @PostMapping("/save")
    public ResponseEntity<Character> save(@RequestBody Character character) {
        characterService.save(character);
        logger.info("Character saved successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(character);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Character> patch(@RequestBody Character character) {
        characterService.patch(character);
        logger.info("Character patched successfully!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(character);
    }

    @GetMapping("/all")
    public List<Character> findAll() {
        logger.info("Find all characters");
        return characterService.getAllCharacters();
    }

    @GetMapping("/id/{id}")
    public Optional<Character> findByID(@PathVariable Integer id) {
        logger.info("Find the character with id: " + id);
        return characterService.getCharacterById(id);
    }


    @GetMapping("/name/{name}")
    public List<Character> findAllWithName(@PathVariable String name) {
        return characterService.getCharactersByName(name);
    }

    @GetMapping("/withFruits")
    public List<Character> findAllWithFruits() {
        return characterService.findWithFruit();
    }

    @GetMapping("/withoutFruits")
    public List<Character> findAllWithoutFruits() {
        return characterService.findWithoutFruit();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        logger.info("Deleted character with id: " + id);
        characterService.delete(id);
    }
}
