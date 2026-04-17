package com.dac.passwordmanager.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import com.dac.passwordmanager.config.security.AesCipherConfig;
import com.dac.passwordmanager.dto.response.CredentialsDTO;
import com.dac.passwordmanager.entity.PasswordCredential;
import com.dac.passwordmanager.repository.PasswordCredentialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PasswordCredentialService {

    private final PasswordCredentialRepository passwordCredentialRepository;
    private final AesCipherConfig aesCipherConfig;

    /*
     * [LEGACY — kept for changePassword re-encryption flow in UserService]
     * Get all passwords for a user and decrypt them server-side.
     */
    @Transactional
    public List<CredentialsDTO> findByUser(Long userId, SecretKey secretKey) throws Exception {
        List<PasswordCredential> passwords = getAllCredentials(userId);

        return passwords.stream().map(password -> {
            CredentialsDTO dto = new CredentialsDTO();
            dto.setId(password.getId());
            dto.setSite(password.getSite());
            dto.setUsername(password.getUsername());
            try {
                dto.setPassword(aesCipherConfig.DecipherPassword(password.getPassword(), secretKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dto.setCreatedAt(password.getCreatedAt());
            dto.setUpdatedAt(password.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    /*
     * REST API method — returns credentials with the password as stored (AES-GCM Base64).
     * Decryption is performed client-side in the Vue frontend using WebCrypto API.
     */
    public List<CredentialsDTO> findRawByUser(Long userId) {
        return getAllCredentials(userId).stream().map(pc -> {
            CredentialsDTO dto = new CredentialsDTO();
            dto.setId(pc.getId());
            dto.setSite(pc.getSite());
            dto.setUsername(pc.getUsername());
            dto.setPassword(pc.getPassword());  // encrypted — frontend decrypts
            dto.setCreatedAt(pc.getCreatedAt());
            dto.setUpdatedAt(pc.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<PasswordCredential> getAllCredentials(Long userId) {
        return passwordCredentialRepository.findByUserId(userId);
    }

    public Optional<PasswordCredential> getCredentialById(UUID id) {
        return passwordCredentialRepository.findById(id);
    }

    public void deleteCredentialById(UUID id) {
        passwordCredentialRepository.deleteById(id);
    }

    public PasswordCredential save(PasswordCredential passwordCredential) {
        return passwordCredentialRepository.save(passwordCredential);
    }
}

