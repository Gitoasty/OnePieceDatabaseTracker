package pwa.project.one_piece.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Integer id;

    @Getter
    @Column(name="name", nullable = false)
    private String name;

    @Column(name="chapterIntroduced")
    private String chapterIntroduced;

    @Column(name="episodeIntroduced")
    private String episodeIntroduced;

    @Column(name="yearIntroduced", nullable = false)
    private String yearIntroduced;

    @Column(name="note")
    private String note;

    @Column(name="master", nullable = false)
    private boolean master;

    @OneToMany(mappedBy="character", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Fruit> fruits = new ArrayList<>();

    public boolean masterGetter() {
        return master;
    }
}
