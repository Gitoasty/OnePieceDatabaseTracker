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

    @Column(name="description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name="characterID")
    private Character character;
}
