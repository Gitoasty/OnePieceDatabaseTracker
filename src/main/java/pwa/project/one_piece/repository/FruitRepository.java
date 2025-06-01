package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Fruit;

import java.util.List;
import java.util.Optional;

/**
 * <h1>
 *     Repository interface for devil fruits
 * </h1>
 */
@Repository
public interface FruitRepository extends JpaRepository<Fruit, Integer> {

    /**
     * <h2>
     *     Method for finding fruits by name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query. Returns list of fruits if found
     * </p>
     * @param name {@link String} for fruit name
     * @return {@link List} containing fruits if found
     */
    List<Fruit> findByNameIgnoreCase(String name);

    /**
     * <h2>
     *     Method for finding fruits by name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query. Returns fruit if found. Had to do it this way because
     *     FruitService was being picky about the used methods
     * </p>
     * @param name {@link String} for fruit name
     * @return {@link Fruit} containing fruit if one is found
     */
    Fruit findByName(String name);

    /**
     * <h2>
     *     Method for finding fruits which have users
     * </h2>
     * <p>
     *     Passes custom sql query to JPA to be executed. Returns list of fruits which have fruit users
     * </p>
     * @return {@link List} of fruits with users
     */
    List<Fruit> findByCharacterIsNotNull();

    /**
     * <h2>
     *     Method for finding fruits which don't have users
     * </h2>
     * <p>
     *     Passes custom sql query to JPA to be executed. Returns list of fruits which don't have fruit users
     * </p>
     * @return {@link List} of fruits without users
     */
    List<Fruit> findByCharacterIsNull();

    /**
     * <h2>
     *     Method for deleting fruits by name
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query for deleting fruit
     * </p>
     * @param name {@link String} name of fruit to be deleted
     */
    void deleteByName(String name);
}
