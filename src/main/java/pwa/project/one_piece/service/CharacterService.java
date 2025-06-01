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

/**
 * <h1>
 *     Service class for characters
 * </h1>
 */
@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    /**
     * <h2>
     *     Method for saving characters in the database
     * </h2>
     * <p>
     *     Checks whether the user is of the Marshall D. family. If so, sets master to true before saving.
     *     Calls the {@link CharacterRepository#findByNameContainingIgnoreCase(String)} method to check if character exists.
     *     If no such character exists, saves it.
     * </p>
     * @param character {@link Character} object to be saved
     */
    public void save(Character character) {
        character.setMaster("Marshall D".equalsIgnoreCase(character.getName()));

        if (characterRepository.findByNameContainingIgnoreCase(character.getName()).isEmpty()) {
            characterRepository.save(character);
        }
    }

    /**
     * <h2>
     *     Method for updating character entries
     * </h2>
     * <p>
     *     First calls {@link CharacterRepository#findByName(String)} to get entry to be updated.
     *     Sets the fields which are not null in the passed object to a new value and saves the entry.
     * </p>
     * @param character {@link Character} object to be updated in the database
     */
    public void patch(Character character) {
        Character patched = characterRepository.findByName(character.getName()).get();

        if (character.getName() != null) patched.setName(character.getName());
        if (character.getChapterIntroduced() != null) patched.setChapterIntroduced(character.getChapterIntroduced());
        if (character.getEpisodeIntroduced() != null) patched.setEpisodeIntroduced(character.getEpisodeIntroduced());
        if (character.getYearIntroduced() != null) patched.setYearIntroduced(character.getYearIntroduced());
        if (character.getNote() != null) patched.setNote(character.getNote());

        characterRepository.save(patched);
    }

    /**
     * <h2>
     *     Method for finding all characters in the database
     * </h2>
     * <p>
     *     Gets all the characters in the database and returns a List containing them.
     * </p>
     * @return {@link List<Character>} containing all characters from database
     */
    public List<Character> getAllCharacters() {
        return characterRepository.findAll().stream().sorted().collect(Collectors.toList());
    }

    /**
     * <h2>
     *     Method for finding all characters whose name contains passed String
     * </h2>
     * <p>
     *     Gets all the characters in the database whose name contain the String and returns a List containing them.
     * </p>
     * @return {@link List<Character>} containing all wanted characters from database
     */
    public List<Character> getCharactersByName(String partialName) {
        return characterRepository.findByNameContainingIgnoreCase(partialName);
    }

    /**
     * <h2>
     *     Method for finding a specific character in the database
     * </h2>
     * <p>
     *     Gets the character in the database and returns it.
     * </p>
     * @return {@link Optional<Character>} containing all characters from database
     */
    public Optional<Character> getCharacterByName(String name) {
        return characterRepository.findByName(name);
    }

    /**
     * <h2>
     *     Method for deleting character by name
     * </h2>
     * <p>
     *     Calls the {@link CharacterRepository#deleteByName(String)} method to delete specific character.
     *     Must be transactional for it to work.
     * </p>
     * @param name {@link String} name to be deleted
     */
    @Transactional
    public void delete(String name) {
        characterRepository.deleteByName(name);
    }

    /**
     * <h2>
     *     Method for getting fruit users
     * </h2>
     * <p>
     *     Finds all characters who are fruit users and returns a List containing them.
     * </p>
     * @return {@link List<Character>} containing fruit users
     */
    public List<Character> findWithFruit() {
        return characterRepository.findCharactersWithFruits();
    }

    /**
     * <h2>
     *     Method for getting characters who are not fruit users
     * </h2>
     * <p>
     *     Finds all characters who are not fruit users and returns a List containing them.
     * </p>
     * @return {@link List<Character>} containing characters who are not fruit users
     */
    public List<Character> findWithoutFruit() {
        return characterRepository.findCharactersWithoutFruits();
    }

    /**
     * <h2>
     *     Method for scraping character data
     * </h2>
     * <p>
     *     Disables SSL verification because the target website did not like the app having SSL enabled.<br>
     *     Connects to the target website for scraping using {@link Jsoup#connect(String)}. Sets a custom user agent<br>
     *     when connecting just in case the website is suspicious about it being a bot. When connected, pulls the html<br>
     *     document. Once it has the html, targets the table with specific style classes. If it found a table, selects<br>
     *     all rows. Skipping the header row, the loop iterates over the other rows and pulls the columns from rows<br>
     *     with 6 or more columns. Once all data is pulled, SSL is enabled and characters are saved to the database.
     * </p>
     * @throws IOException in case connection to the website does not succeed
     */
    public void scrapeAndSaveCharacters() throws IOException {
        SslUtil.disableSslVerification();
        String url = "https://onepiece.fandom.com/wiki/List_of_Canon_Characters";
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .timeout(10000)
                .get();

        List<Character> characters = new ArrayList<>();

        Element table = doc.selectFirst("table.fandom-table.sortable");

        if (table != null) {
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
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

        characterRepository.saveAll(characters);
    }
}
