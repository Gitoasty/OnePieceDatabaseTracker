package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
}
