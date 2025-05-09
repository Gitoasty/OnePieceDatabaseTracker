package pwa.project.one_piece.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "appUser")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
}