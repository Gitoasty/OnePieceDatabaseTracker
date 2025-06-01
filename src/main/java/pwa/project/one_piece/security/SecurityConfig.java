package pwa.project.one_piece.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pwa.project.one_piece.service.AppUserService;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * <h1>
 *     Class for configuring Spring Security
 * </h1>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AppUserService appUserService;

    /**
     * <h2>
     *     Method for setting role permissions for api patterns
     * </h2>
     * <p>
     *     Gives permission to both roles sending GET requests to /api/** patterns.
     *     Sets permissions for other requests for admin role.
     * </p>
     * @param http {@link HttpSecurity} for setting permissions
     * @return {@link SecurityFilterChain} used for filtering permissions
     * @throws Exception in case of error in one of the steps
     */
    @Bean
    @Order(1)
    @ConditionalOnProperty(value = "module.enabled", havingValue = "basic", matchIfMissing = true)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")))
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
                .build();
    }

    /**
     * <h2>
     *     Secondary method for setting role permissions for api patterns
     * </h2>
     * <p>
     *     Permits direct access to folders with css and images. Allows everyone to access the login page and logout requests.
     * </p>
     * @param http {@link HttpSecurity} for setting permissions
     * @return {@link SecurityFilterChain} used for filtering permissions
     * @throws Exception in case of error in one of the steps
     */
    @Bean
    @Order(2)
    public SecurityFilterChain formLoginChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler())
                .permitAll()
        );

        http.logout(logout -> logout.permitAll());

        http.addFilterBefore(new CreateUserIfNotExistsFilter(appUserService, passwordEncoder()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * <h2>
     *     Method for creating custom success handler
     * </h2>
     * <p>
     *     Calls the constructor for CustomLoginSuccessHandler and returns it
     * </p>
     * @return {@link CustomLoginSuccessHandler} to be used in the filter chain
     */
    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    /**
     * <h2>
     *     Custom authentication provider
     * </h2>
     * <p>
     *     Creates DaoAuthenticationProvider and passes in the {@link CustomUserDetailsService} in its constructor.
     *     Sets the password encoder to the custom one and returns the authentication provider.
     * </p>
     * @return {@link DaoAuthenticationProvider} with custom parameters
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new CustomUserDetailsService(appUserService, passwordEncoder()));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * <h2>
     *     Method for creating password encoder
     * </h2>
     * <p>
     *     Creates a BCryptPasswordEncoder and returns it
     * </p>
     * @return {@link BCryptPasswordEncoder} to be set as the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}