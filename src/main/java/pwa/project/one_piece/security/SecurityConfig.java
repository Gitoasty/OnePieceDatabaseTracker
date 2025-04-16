package pwa.project.one_piece.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.repository.UserRepository;
import pwa.project.one_piece.service.AppUserService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AppUserService appUserService;

    @Bean
    @ConditionalOnProperty(value="module.enabled", havingValue = "basic", matchIfMissing = true)
    public SecurityFilterChain filterChainBasic(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN");  // GET for USER & ADMIN
                    auth.requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("USER", "ADMIN"); // POST for USER & ADMIN
                    auth.requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN");  // PUT only for ADMIN
                    auth.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN"); // DELETE only for ADMIN
                    auth.anyRequest().authenticated(); // Ensure all other requests are authenticated


                })
                .csrf(csrf-> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")))
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
                .build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        List<AppUser> existingUsers = appUserService.getAllUsers();
        List<UserDetails> users = new ArrayList<>();
        for (AppUser u : existingUsers) {
            users.add(User.withUsername("user")
                    .password(passwordEncoder().encode("1234"))
                    .roles("USER")
                    .build());
        }
        users.add(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("adminpass"))
                        .roles("ADMIN")
                        .build()
        );
        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
