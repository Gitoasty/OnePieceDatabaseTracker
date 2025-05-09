package pwa.project.one_piece.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.service.AppUserService;

import java.io.IOException;

public class CreateUserIfNotExistsFilter extends OncePerRequestFilter {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    public CreateUserIfNotExistsFilter(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Only process POST requests to login
        if (request.getMethod().equals("POST") && request.getRequestURI().endsWith("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Skip admin user
            if (username != null && !username.equals("admin") && password != null) {
                try {
                    // Check if user exists
                    appUserService.findByUsername(username);
                } catch (UsernameNotFoundException e) {
                    // User doesn't exist, create new user
                    AppUser newUser = new AppUser();
                    newUser.setUsername(username);
                    newUser.setPassword(passwordEncoder.encode(password));
                    appUserService.saveUser(newUser);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}