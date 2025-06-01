package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Character;

import java.util.List;
import java.util.Optional;

/**
 * <h1>
 *     Repository interface for characters
 * </h1>
 */
@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    /**
     * <h2>
     *     Method for finding characters by name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query. Returns character if found
     * </p>
     * @param name {@link String} for character name
     * @return {@link Optional} containing character if one is found
     */
    Optional<Character> findByName(String name);

    /**
     * <h2>
     *     Method for finding characters by part of name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query. Returns character if found
     * </p>
     * @param partialName {@link String} for character name
     * @return {@link Optional} containing character if one is found
     */
    List<Character> findByNameContainingIgnoreCase(String partialName);

    /**
     * <h2>
     *     Method for finding characters which are fruits users
     * </h2>
     * <p>
     *     Passes custom sql query to JPA to be executed. Returns list of characters which are fruit users
     * </p>
     * @return {@link List} of fruit users
     */
    @Query("SELECT c FROM Character c WHERE size(c.fruits) > 0")
    List<Character> findCharactersWithFruits();

    /**
     * <h2>
     *     Method for finding characters which are fruits users
     * </h2>
     * <p>
     *     Passes custom sql query to JPA to be executed. Returns list of characters which are not fruit users
     * </p>
     * @return {@link List} of characters which are not fruit users
     */
    @Query("SELECT c FROM Character c WHERE size(c.fruits) = 0")
    List<Character> findCharactersWithoutFruits();

    /**
     * <h2>
     *     Method for deleting characters by name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query for deleting character
     * </p>
     * @param name {@link String} name of character to be deleted
     */
    void deleteByName(String name);
}
