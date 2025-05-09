package pwa.project.one_piece.service;

import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.repository.CharacterRepository;
import pwa.project.one_piece.security.SslUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public void save(Character character) {
        character.setMaster("Marshall D".equalsIgnoreCase(character.getName()));

        if (characterRepository.findByNameContainingIgnoreCase(character.getName()).isEmpty()) {
            characterRepository.save(character);
        }
    }

    public Character patch(Character character) {
        Character patched = characterRepository.findByName(character.getName()).get();

        if (character.getName() != null) patched.setName(character.getName());
        if (character.getChapterIntroduced() != null) patched.setChapterIntroduced(character.getChapterIntroduced());
        if (character.getEpisodeIntroduced() != null) patched.setEpisodeIntroduced(character.getEpisodeIntroduced());
        if (character.getYearIntroduced() != null) patched.setYearIntroduced(character.getYearIntroduced());
        if (character.getNote() != null) patched.setNote(character.getNote());

        return characterRepository.save(character);
    }

    public List<Character> getAllCharacters() {
        return characterRepository.findAll().stream().sorted().collect(Collectors.toList());
    }

    public List<Character> getCharactersByName(String partialName) {
        return characterRepository.findByNameContainingIgnoreCase(partialName);
    }

    public Optional<Character> getCharacterByName(String name) {
        return characterRepository.findByName(name);
    }

    @Transactional
    public void delete(String name) {
        characterRepository.deleteByName(name);
    }

    public List<Character> findWithFruit() {
        return characterRepository.findCharactersWithFruits();
    }

    public List<Character> findWithoutFruit() {
        return characterRepository.findCharactersWithoutFruits();
    }

    public List<Character> scrapeAndSaveCharacters() throws IOException {
        SslUtil.disableSslVerification();
        String url = "https://onepiece.fandom.com/wiki/List_of_Canon_Characters";
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .timeout(10000) // 10 seconds
                .get();

        List<Character> characters = new ArrayList<>();

       Element table = doc.selectFirst("table.fandom-table.sortable");

        Elements tables = doc.select("table");

        if (table != null) {
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { // skip header row
                Elements cols = rows.get(i).select("td");

                if (cols.size() >= 6) {
                    String name = cols.get(1).text();
                    String chapter = cols.get(2).text();
                    String episode = cols.get(3).text();
                    String year = cols.get(4).text();
                    String note = cols.get(5).text();

                    Character character = new Character();
                    character.setName(name);
                    character.setChapterIntroduced(chapter);
                    character.setEpisodeIntroduced(episode);
                    character.setYearIntroduced(year);
                    character.setNote(note);
                    characters.add(character);
                }
            }
        }

        SslUtil.reenableSslVerification();

        return characterRepository.saveAll(characters);
    }
}
