package com.dac.passwordmanager.repository;

import com.dac.passwordmanager.entity.PasswordResetToken;
import com.dac.passwordmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}
