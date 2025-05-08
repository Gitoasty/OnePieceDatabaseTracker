package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Character;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    Optional<Character> findByName(String name);

    List<Character> findByNameContainingIgnoreCase(String partialName);

    @Query("SELECT c FROM Character c WHERE size(c.fruits) > 0")
    List<Character> findCharactersWithFruits();

    @Query("SELECT c FROM Character c WHERE size(c.fruits) = 0")
    List<Character> findCharactersWithoutFruits();

    @Query("DELETE c FROM Character c WHERE c.name = name")
    void deleteByName(String name);
}
