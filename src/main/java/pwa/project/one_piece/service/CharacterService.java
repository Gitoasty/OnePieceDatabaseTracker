package pwa.project.one_piece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.repository.CharacterRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Character save(Character character) {
        character.setMaster("Marshall D".equalsIgnoreCase(character.getName()));

        return characterRepository.save(character);
    }

    public Optional<Character> getUserById(Integer id) {
        return characterRepository.findById(id);
    }

    public List<Character> getAllUsers() {
        return characterRepository.findAll();
    }

    public Optional<Character> getUserByName(String name) {
        return characterRepository.findByName(name);
    }

    public List<Character> searchCharactersByName(String partialName) {
        return characterRepository.findByNameContainingIgnoreCase(partialName);
    }

    public void delete(Integer id) {
        characterRepository.deleteById(id);
    }

    public List<Character> findWithFruit() {
        return characterRepository.findCharactersWithFruits();
    }

    public List<Character> findWithoutFruit() {
        return characterRepository.findCharactersWithoutFruits();
    }
}
