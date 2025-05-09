package pwa.project.one_piece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.repository.AppUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean existsByUsername(String username) {
        return appUserRepository.findByUsername(username).isPresent();
    }

    public AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }
}