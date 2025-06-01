package pwa.project.one_piece.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

/**
 * <h1>
 *     Custom handler class for login success
 * </h1>
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * <h2>
     *     Handles successful logins
     * </h2>
     * <p>
     *     Redirects admin to the admin tree and users to the user tree
     * </p>
     * @param request {@link HttpServletRequest} from the servlet
     * @param response {@link HttpServletResponse} from the servlet
     * @param authentication {@link Authentication} for accessing authorities
     * @throws IOException if unable to access utilities
     * @throws ServletException if servlet is incorrect
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin-main");
        } else {
            response.sendRedirect("/user-main");
        }
    }
}