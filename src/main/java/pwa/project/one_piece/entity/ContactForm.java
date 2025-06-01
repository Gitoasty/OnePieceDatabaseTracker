package pwa.project.one_piece.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>
 *     Custom class for handling contact forms
 * </h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {
    private String name;
    private String mail;
    private Integer broj;
}
