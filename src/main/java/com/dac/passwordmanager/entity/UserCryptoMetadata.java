package com.dac.passwordmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_crypto_metadata")
public class UserCryptoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "encrypted_vault_key", columnDefinition = "TEXT")
    private String encryptedVaultKey;

    @Column(name = "recovery_vault_key", columnDefinition = "TEXT")
    private String recoveryVaultKey;

    public UserCryptoMetadata() {
    }
}
