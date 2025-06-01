package pwa.project.one_piece.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <h1>
 *     Entity class describing character
 * </h1>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character implements Comparable<Character> {
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

    @OneToMany(mappedBy="character")
    @Column(name="fruits", nullable = true)
    private List<Fruit> fruits = new ArrayList<>();

    /**
     * <h2>
     *     Custom getter for master property
     * </h2>
     * <p>
     *     Written because lombok getter was giving incorrect returns
     * </p>
     * @return boolean value, true if character is master
     */
    public boolean masterGetter() {
        return master;
    }

    /**
     * <h2>
     *     Custom compareTo method for alphabetical sorting
     * </h2>
     * <p>
     *     Created for proper handling of alphabetical sorting. Properly handles uncommon characters (umlauts and alike)
     * </p>
     * @param o the object to be compared.
     * @return int value, 0 or 1, depends on which is first
     */
    @Override
    public int compareTo(@NotNull Character o) {
        if (this.name == null) return (o.getName() == null) ? 0 : -1;
        if (o.getName() == null) return 1;

        String thisName = this.name.trim().replaceAll("\\s+", " ");
        String otherName = o.getName().trim().replaceAll("\\s+", " ");

        Collator collator = Collator.getInstance(Locale.ROOT);
        collator.setDecomposition(Collator.FULL_DECOMPOSITION);

        collator.setStrength(Collator.PRIMARY);
        int result = collator.compare(thisName, otherName);

        if (result == 0) {
            collator.setStrength(Collator.SECONDARY);
            result = collator.compare(thisName, otherName);

            if (result == 0) {
                collator.setStrength(Collator.TERTIARY);
                result = collator.compare(thisName, otherName);

                if (result == 0 && !this.name.equals(o.getName())) {
                    return this.name.compareTo(o.getName());
                }
            }
        }

        return result;
    }
}
