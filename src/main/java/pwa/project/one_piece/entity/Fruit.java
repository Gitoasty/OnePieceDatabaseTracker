package pwa.project.one_piece.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * <h1>
 *     Entity class for describing devil fruits
 * </h1>
 */
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
    @JoinColumn(name="character", nullable = true)
    private Character character;

    /**
     * <h2>
     *     Custom toString method
     * </h2>
     * <p>
     *     Creates a custom string depending on object properties
     * </p>
     * @return {@link String} describing object
     */
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
