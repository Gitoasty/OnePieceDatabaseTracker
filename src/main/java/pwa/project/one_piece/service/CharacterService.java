package pwa.project.one_piece.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.Character;
import pwa.project.one_piece.repository.CharacterRepository;
import pwa.project.one_piece.security.SslUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

    public List<Character> scrapeAndSaveCharacters() throws IOException {
        SslUtil.disableSslVerification();
        String url = "https://onepiece.fandom.com/wiki/List_of_Canon_Characters";
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .timeout(10000) // 10 seconds
                .get();

        List<Character> characters = new ArrayList<>();

        // Find the first table with class "wikitable"
       Element table = doc.selectFirst("table.fandom-table.sortable");

        Elements tables = doc.select("table");
        System.out.println("Total tables found: " + tables.size());

        for (Element t : tables) {
            System.out.println("Table classes: " + t.className());
        }

        if (table != null) {
            System.out.println("a");
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { // skip header row
                Elements cols = rows.get(i).select("td");
                System.out.println(cols);

                if (cols.size() >= 5) {
                    String name = cols.get(0).text();
                    String chapter = cols.get(1).text();
                    String episode = cols.get(2).text();
                    String year = cols.get(3).text();
                    String note = cols.get(4).text();

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
        System.out.println(characters);

        SslUtil.reenableSslVerification();

        return characterRepository.saveAll(characters);
    }
}
