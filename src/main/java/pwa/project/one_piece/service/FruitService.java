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

/**
 * <h1>
 *     Service class for fruits
 * </h1>
 */
@Service
public class FruitService {

    @Autowired
    private FruitRepository fruitRepository;
    @Autowired
    private CharacterRepository characterRepository;

    /**
     * <h2>
     *     Method for saving fruits in the database
     * </h2>
     * <p>
     *     Calls the {@link FruitRepository#findByNameIgnoreCase(String)} method to check if fruit exists.
     *     If no such fruit exists, saves it. Must be transactional.
     * </p>
     * @param fruit {@link Fruit} object to be saved
     */
    @Transactional
    public void save(Fruit fruit) {
        if (fruitRepository.findByNameIgnoreCase(fruit.getName()).isEmpty()) {
            fruitRepository.save(fruit);
        }
    }

    /**
     * <h2>
     *     Method for binding fruits to characters
     * </h2>
     * <p>
     *     Tries finding character with given name. If one is found, checks whether the character is marked as master or
     *     does not have a fruit already. In that case, checks that the fruit has no user or the user is not already set
     *     to the character. Removes the fruit from the old character if one is found. Sets the new character for the fruit
     *     and sets the fruit for the character if the fruit isn't already assigned to it. Must be transactional.
     * </p>
     * @param fruit {@link Fruit} object to be associated
     * @param characterName {@link String} name of character to be bound to the fruit
     */
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

    /**
     * <h2>
     *     Method to update fruit character
     * </h2>
     * <p>
     *     Simply calls the associate method. Done like this to catch the exceptions.
     *     Must be transactional.
     * </p>
     * @param fruit {@link Fruit} object to be associated
     * @param characterName {@link String} name of character to be bound to the fruit
     */
    @Transactional
    public void updateFruitCharacter(Fruit fruit, String characterName) {
        try {
            associateFruitWithCharacter(fruit, characterName);
        } catch (Exception ignored) {}
    }

    /**
     * <h2>
     *     Method for updating fruit
     * </h2>
     * <p>
     *     Calls the {@link FruitRepository#save} method to put the {@link Fruit} into the database.
     *     Must be transactional
     * </p>
     * @param fruit {@link Fruit} to be saved
     */
    @Transactional
    public void update(Fruit fruit) {
        fruitRepository.save(fruit);
    }

    /**
     * <h2>
     *     Method for fetching all devil fruits
     * </h2>
     * <p>
     *     Gets all devil fruits by calling {@link FruitRepository#findAll()}.
     * </p>
     * @return {@link List<Fruit>} of all saved devil fruits
     */
    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    /**
     * <h2>
     *     Method for fetching all devil fruits containing part of name
     * </h2>
     * <p>
     *     Gets all devil fruits by calling {@link FruitRepository#findByNameIgnoreCase(String)}.
     * </p>
     * @return {@link List<Fruit>} of all such devil fruits
     */
    public List<Fruit> getFruitByName(String name) {
        return fruitRepository.findByNameIgnoreCase(name);
    }

    /**
     * <h2>
     *     Method for fetching all devil fruits with assigned users
     * </h2>
     * <p>
     *     Gets all devil fruits by calling {@link FruitRepository#findByCharacterIsNotNull()}.
     * </p>
     * @return {@link List<Fruit>} of all such devil fruits
     */
    public List<Fruit> getFruitsWithUsers() {
        return fruitRepository.findByCharacterIsNotNull();
    }

    /**
     * <h2>
     *     Method for fetching all devil fruits without assigned users
     * </h2>
     * <p>
     *     Gets all devil fruits by calling {@link FruitRepository#findByCharacterIsNull()}.
     * </p>
     * @return {@link List<Fruit>} of all such devil fruits
     */
    public List<Fruit> getFruitsWithoutUsers() {
        return fruitRepository.findByCharacterIsNull();
    }

    /**
     * <h2>
     *     Method for deleting devil fruit by name
     * </h2>
     * <p>
     *     If fruit with passed name exists, checks whether it's assigned to a character. If it has a character,
     *     removes the fruit from that character and sets the fruit's character to null. At the end deletes the fruit.
     *     Must be transactional.
     * </p>
     * @param name {@link String} name of the fruit to delete
     */
    @Transactional
    public void delete(String name) {
        Fruit fruit = fruitRepository.findByName(name);
        if (fruit != null) {
            if (fruit.getCharacter() != null) {
                Character character = fruit.getCharacter();
                character.getFruits().remove(fruit);
                fruit.setCharacter(null);
            }
            fruitRepository.deleteByName(name);
        }
    }

    /**
     * <h2>
     *     Method for adding devil fruits to the database
     * </h2>
     * <p>
     *     If fruit with same name does not exist, creates a {@link Fruit} object to be saved. If passed character name
     *     is not null and a character with that name exists, it is associated with the fruit.
     *     The Fruit object is saved in the end. Must be transactional.
     * </p>
     * @param fruitName {@link String} name of the devil fruit
     * @param fruitPower {@link String} power of the fruit
     * @param awakened {@link String} awakened power of the fruit
     * @param weakness {@link String} weakness of the fruit
     * @param fruitType {@link FruitType} type of devil fruit
     * @param characterName {@link String} name of character to be assigned
     */
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