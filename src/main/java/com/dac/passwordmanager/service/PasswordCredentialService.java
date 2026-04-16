package com.dac.passwordmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
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
     * Get all passwords from a user
     * 
     * @param userId
     * 
     * @param secretKey
     * 
     * @return List<CredentialsDTO>
     */
    @Transactional
    public List<CredentialsDTO> findByUser(Long userId, SecretKey secretKey) throws Exception {
        List<PasswordCredential> passwords = getAllCredentials(userId);

        return passwords.stream().map(password -> {
            CredentialsDTO passwordDTO = new CredentialsDTO();
            passwordDTO.setId(password.getId());
            passwordDTO.setSite(password.getSite());
            passwordDTO.setUsername(password.getUsername());
            try {
                passwordDTO.setPassword(
                        aesCipherConfig.DecipherPassword(password.getPassword(), secretKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            passwordDTO.setCreatedAt(password.getCreatedAt());
            passwordDTO.setUpdatedAt(password.getUpdatedAt());
            return passwordDTO;
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
