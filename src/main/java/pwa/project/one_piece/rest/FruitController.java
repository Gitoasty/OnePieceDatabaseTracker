package pwa.project.one_piece.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwa.project.one_piece.entity.Fruit;
import pwa.project.one_piece.service.FruitService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fruit")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    Logger logger = LoggerFactory.getLogger(FruitController.class);

    @PostMapping("/save")
    public ResponseEntity<Fruit> save(@RequestBody Fruit fruit) {
        fruitService.save(fruit);
        logger.info("Saved fruit: " + fruit.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(fruit);
    }

    @GetMapping("/all")
    public List<Fruit> findAll() {
        return fruitService.getAllFruits();
    }

    @GetMapping("/id/{id}")
    public Optional<Fruit> findByID(@PathVariable Integer id) {
        logger.info("Finds the fruit with id: " + id);
        return fruitService.getFruitById(id);
    }

    @GetMapping("/name/{name}")
    public List<Fruit> findByName(@PathVariable String name) {
        logger.info("Finds all fruits containing: " + name);
        return fruitService.getFruitByName(name);
    }

    @GetMapping("/withUsers")
    public List<Fruit> getFruitsWithUsers() {
        logger.info("Finds all fruits with users");
        return fruitService.getFruitsWithUsers();
    }

    @GetMapping("/withoutUsers")
    public List<Fruit> getFruitsWithoutUsers() {
        logger.info("Finds all fruits without users");
        return fruitService.getFruitsWithoutUsers();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        logger.info("Deleted fruit with id: " + id);
        fruitService.delete(id);
    }
}
