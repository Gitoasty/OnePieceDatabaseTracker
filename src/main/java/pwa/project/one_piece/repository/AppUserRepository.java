package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.AppUser;

import java.util.Optional;

/**
 * <h1>
 *     Repository interface for app users
 * </h1>
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    /**
     * <h2>
     *     Search method for app user table
     * </h2>
     * <p>
     *     Passes parameter to JPA, which executes a query. Returns user if found
     * </p>
     * @param username {@link String} for username
     * @return {@link Optional} containing user if one is found
     */
    Optional<AppUser> findByUsername(String username);
}