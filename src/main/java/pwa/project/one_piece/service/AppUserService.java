package pwa.project.one_piece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.repository.AppUserRepository;

/**
 * <h1>
 *     Service class for app users
 * </h1>
 */
@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * <h2>
     *     Method for finding users by name
     * </h2>
     * <p>
     *     Calls the repository method for finding users by username. In case a user has been found, returns it.
     *     In case it does not find a user, throws {@link UsernameNotFoundException}.
     * </p>
     * @param username {@link String} describing desired username
     * @return {@link AppUser} if it exists
     */
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * <h2>
     * Method for saving users
     * </h2>
     * <p>
     * Calls the repository method for saving users in the database. Returns the saved AppUser object.
     * </p>
     *
     * @param user {@link AppUser} object to be saved
     */
    public void saveUser(AppUser user) {
        appUserRepository.save(user);
    }
}