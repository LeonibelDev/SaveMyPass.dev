package com.dac.passwordmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dac.passwordmanager.entity.PasswordCredential;
import com.dac.passwordmanager.repository.PasswordCredentialRepository;

@Service
public class PasswordCredentialService {

    private final PasswordCredentialRepository passwordCredentialRepository;

    @Autowired
    public PasswordCredentialService(PasswordCredentialRepository passwordCredentialRepository) {
        this.passwordCredentialRepository = passwordCredentialRepository;
    }

    public List<PasswordCredential> findByUser(String tarjetaEmpleado) {
        return passwordCredentialRepository.findByUserTarjetaEmpleado(tarjetaEmpleado);
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
