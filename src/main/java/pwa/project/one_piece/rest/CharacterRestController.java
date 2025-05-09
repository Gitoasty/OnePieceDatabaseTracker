//package pwa.project.one_piece.rest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import pwa.project.one_piece.entity.Character;
//import pwa.project.one_piece.service.CharacterService;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/character")
//public class CharacterRestController {
//
//    @Autowired
//    private CharacterService characterService;
//
//    Logger logger = LoggerFactory.getLogger(CharacterRestController.class);
//
//
//    @GetMapping("/all")
//    public List<Character> findAll() {
//        logger.info("Find all characters");
//        return characterService.getAllCharacters();
//    }
//
//    @GetMapping("/id/{id}")
//    public Optional<Character> findByID(@PathVariable Integer id) {
//        logger.info("Find the character with id: " + id);
//        return characterService.getCharacterById(id);
//    }
//
//
//    @GetMapping("/name/{name}")
//    public List<Character> findAllWithName(@PathVariable String name) {
//        return characterService.getCharactersByName(name);
//    }
//}
