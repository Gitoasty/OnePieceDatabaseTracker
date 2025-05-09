package pwa.project.one_piece.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwa.project.one_piece.entity.Fruit;

import java.util.List;

@Repository
public interface FruitRepository extends JpaRepository<Fruit, Integer> {

    List<Fruit> findByNameIgnoreCase(String name);

    Fruit findByName(String name);

    List<Fruit> findByCharacterIsNotNull();

    List<Fruit> findByCharacterIsNull();

    void deleteByName(String name);
}
