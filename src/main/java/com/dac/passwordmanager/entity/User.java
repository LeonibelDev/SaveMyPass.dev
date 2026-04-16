package com.dac.passwordmanager.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private List<PasswordCredential> passwordCredentials;

    public User() {
    }

}