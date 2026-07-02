package com.dac.passwordmanager.repository;

import com.dac.passwordmanager.entity.UserCryptoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCryptoMetadataRepository extends JpaRepository<UserCryptoMetadata, Long> {
    Optional<UserCryptoMetadata> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
