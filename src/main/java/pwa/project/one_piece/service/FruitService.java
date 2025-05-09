package pwa.project.one_piece.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.entity.Fruit;
import pwa.project.one_piece.entity.FruitType;
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

    @Transactional
    public void save(Fruit fruit) {
        if (fruitRepository.findByNameIgnoreCase(fruit.getName()).isEmpty()) {
            fruitRepository.save(fruit);
        }
    }

    @Transactional
    private void associateFruitWithCharacter(Fruit fruit, String characterName) {
        Optional<Character> characterOpt = characterRepository.findByName(characterName);

        if (characterOpt.isPresent()) {
            Character character = characterOpt.get();

            if (character.masterGetter() || character.getFruits().isEmpty()) {
                if (fruit.getCharacter() != null && !fruit.getCharacter().equals(character)) {
                    Character oldCharacter = fruit.getCharacter();
                    oldCharacter.getFruits().remove(fruit);
                }

                fruit.setCharacter(character);
                if (!character.getFruits().contains(fruit)) {
                    character.getFruits().add(fruit);
                }
            }
        }
    }

    @Transactional
    public void updateFruitCharacter(Fruit fruit, String characterName) {
        try {
            associateFruitWithCharacter(fruit, characterName);
        } catch (Exception ignored) {}
    }

    @Transactional
    public void update(Fruit fruit) {
        fruitRepository.save(fruit);
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

    @Transactional
    public void delete(String name) {
        Fruit fruit = fruitRepository.findByName(name);
        if (fruit != null) {  // Add null check
            if (fruit.getCharacter() != null) {
                Character character = fruit.getCharacter();
                character.getFruits().remove(fruit);
                fruit.setCharacter(null);
            }
            fruitRepository.deleteByName(name);
        }
    }

    @Transactional
    public void addFruit(String fruitName, String fruitPower,
                         String awakened, String weakness,
                         FruitType fruitType, String characterName) {

        if (fruitRepository.findByNameIgnoreCase(fruitName).isEmpty()) {
            Fruit fruit = new Fruit();
            fruit.setName(fruitName);
            fruit.setPower(fruitPower);
            fruit.setAwakened(awakened);
            fruit.setWeakness(weakness);
            fruit.setType(fruitType);

            if (characterName != null && !characterName.trim().isEmpty()) {
                associateFruitWithCharacter(fruit, characterName);
            }

            fruitRepository.save(fruit);
        }
    }
}