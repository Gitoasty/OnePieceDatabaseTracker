package pwa.project.one_piece.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.service.AppUserService;

import java.io.IOException;

/**
 * Custom helper class for filtering users
 */
public class CreateUserIfNotExistsFilter extends OncePerRequestFilter {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    /**
     * <h2>
     *     AllArgs constructor
     * </h2>
     * @param appUserService {@link AppUserService} to be used
     * @param passwordEncoder {@link PasswordEncoder} to be used
     */
    public CreateUserIfNotExistsFilter(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <h2>
     *     Internal filter for users
     * </h2>
     * <p>
     *      If passed POST request, takes username and password. In case username isn't admin,
     *      and user does not exist, creates the user. Used for creating new users directly from
     *      the login form.
     * </p>
     * @param request {@link java.net.http.HttpRequest} for received http request
     * @param response {@link HttpServletResponse} response to be set for the given request
     * @param filterChain {@link FilterChain} for logins
     * @throws ServletException in case of improper servlet
     * @throws IOException in case of problems with database queries
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equals("POST") && request.getRequestURI().endsWith("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username != null && !username.equals("admin") && password != null) {
                try {
                    appUserService.findByUsername(username);
                } catch (UsernameNotFoundException e) {
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