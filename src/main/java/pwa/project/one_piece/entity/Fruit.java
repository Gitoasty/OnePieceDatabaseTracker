package pwa.project.one_piece.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Fruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="power", nullable = false)
    private String power;

    @Column(name="awakened", nullable = true)
    private String awakened;

    @Column(name="weakness", nullable = true)
    private String weakness;

    @Column(name="type", nullable = false)
    private FruitType type;

    @ManyToOne
    @JoinColumn(name="characterID", nullable = true)
    private Character character;

    @Override
    public String toString() {
        return "Fruit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", power='" + power + '\'' +
                ", fruitType=" + type +
                ", characterName=" + (character != null ? character.getName() : null) +
                '}';
    }
}
