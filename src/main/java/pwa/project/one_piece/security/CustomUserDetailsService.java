package pwa.project.one_piece.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.service.AppUserService;

/**
 * <h1>
 *     Custom class for handling user details
 * </h1>
 */
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    /**
     * <h2>
     *     AllArgs constructor
     * </h2>
     * @param appUserService {@link AppUserService} for internal use
     * @param passwordEncoder {@link PasswordEncoder} for internal use
     */
    public CustomUserDetailsService(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <h2>
     *     Method for getting user details by username
     * </h2>
     * <p>
     *     If user name is admin, creates hard coded admin account and returns its details.
     *     If user is not admin, tries finding an existing user.
     *     In case user does not exist, throws {@link UsernameNotFoundException}.
     * </p>
     * @param username {@link String} username for details
     * @return {@link UserDetails} user details for desired user
     * @throws UsernameNotFoundException in case user is not found/doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("adminpass"))
                    .roles("ADMIN")
                    .build();
        }

        try {
            AppUser appUser = appUserService.findByUsername(username);
            return User.withUsername(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles("USER")
                    .build();
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}