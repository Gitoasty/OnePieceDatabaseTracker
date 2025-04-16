package pwa.project.one_piece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwa.project.one_piece.entity.AppUser;
import pwa.project.one_piece.repository.UserRepository;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private UserRepository userRepository;

    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<AppUser> getUsersByName(String name) {
        return userRepository.findAllByUsername(name);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
