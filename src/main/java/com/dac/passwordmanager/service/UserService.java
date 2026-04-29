package com.dac.passwordmanager.service;

import javax.crypto.SecretKey;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final NoteService noteService;
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
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // This is the AuthHash
        user.setSalt(dto.getSalt());

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

    /**
     * Z-K Vault Rotation
     * Updates user's auth hash, salt, and all re-encrypted credentials/notes.
     */
    @Transactional
    public void rotateVault(VaultRotationRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 1. Update User Auth (AuthHash) and Salt
        user.setPassword(passwordEncoder.encode(dto.getNewAuthHash()));
        user.setSalt(dto.getNewSalt());
        userRepository.save(user);

        // 2. Update Credentials (Verify ownership)
        if (dto.getCredentials() != null) {
            for (VaultRotationRequestDTO.CredentialRotationDTO credDto : dto.getCredentials()) {
                PasswordCredential pc = passwordCredentialService.getCredentialById(credDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Credential not found: " + credDto.getId()));
                
                if (!pc.getUser().getId().equals(userId)) {
                    throw new SecurityException("Unauthorized access to credential: " + credDto.getId());
                }

                pc.setSite(credDto.getSite());
                pc.setUsername(credDto.getUsername());
                pc.setPassword(credDto.getPassword());
                pc.setUpdatedAt(java.time.LocalDateTime.now().toString());
                passwordCredentialService.save(pc);
            }
        }

        // 3. Update Notes (Verify ownership)
        if (dto.getNotes() != null) {
            for (VaultRotationRequestDTO.NoteRotationDTO noteDto : dto.getNotes()) {
                Note note = noteService.getNoteById(noteDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Note not found: " + noteDto.getId()));

                if (!note.getUser().getId().equals(userId)) {
                    throw new SecurityException("Unauthorized access to note: " + noteDto.getId());
                }

                note.setTitle(noteDto.getTitle());
                note.setContent(noteDto.getContent());
                note.setCategory(noteDto.getCategory());
                note.setUpdatedAt(java.time.LocalDateTime.now());
                noteService.save(note);
            }
        }
    }

}