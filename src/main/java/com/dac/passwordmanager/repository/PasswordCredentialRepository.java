package com.dac.passwordmanager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dac.passwordmanager.entity.PasswordCredential;

public interface PasswordCredentialRepository extends JpaRepository<PasswordCredential, UUID> {
    List<PasswordCredential> findByUserTarjetaEmpleado(String tarjetaEmpleado);

    Optional<PasswordCredential> findById(UUID id);

    void deleteById(UUID id);
}
