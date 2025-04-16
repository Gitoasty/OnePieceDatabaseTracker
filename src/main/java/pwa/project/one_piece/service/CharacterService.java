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

    public Character patch(Character character) {
        Character patched = characterRepository.findById(character.getId()).get();

        if (character.getName() != null) patched.setName(character.getName());
        if (character.getChapterIntroduced() != null) patched.setChapterIntroduced(character.getChapterIntroduced());
        if (character.getEpisodeIntroduced() != null) patched.setEpisodeIntroduced(character.getEpisodeIntroduced());
        if (character.getYearIntroduced() != null) patched.setYearIntroduced(character.getYearIntroduced());
        if (character.getNote() != null) patched.setNote(character.getNote());

        return characterRepository.save(character);
    }

    public Optional<Character> getCharacterById(Integer id) {
        return characterRepository.findById(id);
    }

    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    public List<Character> getCharactersByName(String partialName) {
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
