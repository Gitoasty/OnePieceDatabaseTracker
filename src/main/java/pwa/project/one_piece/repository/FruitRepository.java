package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Fruit;

@Repository
public interface FruitRepository extends JpaRepository<Fruit, Integer> {
}
