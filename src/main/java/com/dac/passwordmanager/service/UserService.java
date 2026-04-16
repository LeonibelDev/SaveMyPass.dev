package com.dac.passwordmanager.service;

import javax.crypto.SecretKey;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dac.passwordmanager.config.security.*;
import com.dac.passwordmanager.dto.*;
import com.dac.passwordmanager.entity.*;
import com.dac.passwordmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordCredentialService passwordCredentialService;
    private final AesCipherConfig aesCipherConfig;
    private final PasswordEncoder passwordEncoder;

    /*
     * Save new user
     * 
     * @param dto
     * 
     * @return User
     */
    @Transactional
    public User saveNewUser(SignupRequestDTO dto) {
        /*
         * Validate that the passwords match
         */
        if (!dto.getPassword().equals(dto.getPasswordConfirmacion())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        /*
         * Validate that the email does not exist
         */
        Optional<User> verifyUserByEmail = getUserByEmail(dto.getEmail());
        if (verifyUserByEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setNombreCompleto(dto.getNombreCompleto());
        user.setEmail(dto.getEmail());
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setSalt(Base64.getEncoder().encodeToString(KeyUtil.generateSalt()));

        return userRepository.save(user);
    }

    /*
     * Login
     * 
     * @param dto
     * 
     * @return User
     */

    public User login(LoginRequestDTO dto) {
        /*
         * Get user by email
         */
        Optional<User> user = getUserByEmail(dto.getEmail());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        /*
         * Validate password
         */
        if (!passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user.get();

    }

    /*
     * Get user by id
     * 
     * @param id
     * 
     * @return Optional<User>
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /*
     * Get user by email
     * 
     * @param email
     * 
     * @return Optional<User>
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /*
     * Change user salt and password
     * 
     * @param changeUserPassword
     * 
     * @param userId
     * 
     * return SecretKey -> new password + new salt
     */
    @Transactional
    public SecretKey changePassword(ChangeUserPassword changeUserPassword, Long userId, SecretKey secretKey)
            throws Exception {

        /*
         * Get user by id
         */
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        /*
         * Validate current password
         */
        if (!passwordEncoder.matches(changeUserPassword.getOldPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        /*
         * Validate that the new password and the confirmation password match
         */
        if (!changeUserPassword.getNewPassword().equals(changeUserPassword.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        /*
         * Validate password length
         */

        if (changeUserPassword.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        /*
         * Generate new salt
         */
        byte[] salt = KeyUtil.generateSalt();
        String saltBase64 = Base64.getEncoder().encodeToString(salt);

        user.get().setSalt(saltBase64);

        /*
         * Generate new Secret
         */

        SecretKey newSecretKey = KeyUtil.deriveKey(changeUserPassword.getNewPassword(), salt);

        /*
         * set new password with bcrypt
         */
        String newPassword = passwordEncoder.encode(changeUserPassword.getNewPassword());
        user.get().setPassword(newPassword);

        /*
         * Update User
         */

        saveUser(user.get());

        /*
         * Get all user password
         */

        List<PasswordCredential> passwordCredentials = passwordCredentialService.getAllCredentials(userId);

        for (PasswordCredential passwordCredential : passwordCredentials) {
            String decriptedPassword = aesCipherConfig.DecipherPassword(passwordCredential.getPassword(), secretKey);

            passwordCredential.setPassword(
                    aesCipherConfig.CipherPassword(decriptedPassword, newSecretKey));

            passwordCredentialService.save(passwordCredential);
        }

        return newSecretKey;

    }

}