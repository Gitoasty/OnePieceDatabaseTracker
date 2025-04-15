package pwa.project.one_piece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.entity.Fruit;
import pwa.project.one_piece.repository.CharacterRepository;
import pwa.project.one_piece.repository.FruitRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FruitService {

    @Autowired
    private FruitRepository fruitRepository;
    @Autowired
    private CharacterRepository characterRepository;

    public Fruit save(Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    public Fruit assignFruitToUser(Integer characterId, Fruit fruit) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!character.masterGetter() && !character.getFruits().isEmpty()) {
            throw new IllegalStateException("Characters can only use one fruit!!!");
        }

        fruit.setCharacter(character);
        character.getFruits().add(fruit);

        return fruitRepository.save(fruit);
    }

    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    public Optional<Fruit> getFruitById(Integer id) {
        return fruitRepository.findById(id);
    }

    public List<Fruit> getFruitByName(String name) {
        return fruitRepository.findByNameIgnoreCase(name);
    }

    public List<Fruit> getFruitsWithUsers() {
        return fruitRepository.findByCharacterIsNotNull();
    }

    public List<Fruit> getFruitsWithoutUsers() {
        return fruitRepository.findByCharacterIsNull();
    }

    public void delete(Integer id) {
        fruitRepository.deleteById(id);
    }
}
