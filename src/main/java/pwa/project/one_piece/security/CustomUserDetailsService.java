package pwa.project.one_piece.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.service.AppUserService;

public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hard-coded admin account
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("adminpass"))
                    .roles("ADMIN")
                    .build();
        }

        // Try to find existing user
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