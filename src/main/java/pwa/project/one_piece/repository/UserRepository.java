package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwa.project.one_piece.entity.AppUser;

import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

    List<AppUser> findAllByUsername(String username);
}
