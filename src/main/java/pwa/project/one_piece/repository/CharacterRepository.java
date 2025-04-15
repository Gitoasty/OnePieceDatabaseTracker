package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Character;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    Optional<Character> findByName(String name);

    List<Character> findByNameContainingIgnoreCase(String partialName);
}
