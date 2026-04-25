package com.dac.passwordmanager.controller;

import com.dac.passwordmanager.dto.PasswordCredentialRequestDTO;
import com.dac.passwordmanager.dto.response.ApiResponseDTO;
import com.dac.passwordmanager.dto.response.CredentialsDTO;
import com.dac.passwordmanager.entity.MessageEntity;
import com.dac.passwordmanager.entity.PasswordCredential;
import com.dac.passwordmanager.entity.User;
import com.dac.passwordmanager.service.PasswordCredentialService;
import com.dac.passwordmanager.service.UserService;
import com.dac.passwordmanager.service.email.EmailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final PasswordCredentialService passwordCredentialService;
    private final UserService userService;
    private final EmailService emailService;

    /**
     * GET /api/credentials
     * Returns all credentials for the authenticated user.
     * Passwords are returned as stored (AES-GCM Base64 ciphertext).
     * The Vue frontend decrypts them locally using the derived AES key.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CredentialsDTO>>> list(
            @AuthenticationPrincipal Claims claims) {
        Long userId = claims.get("userId", Long.class);

        /*
         * Returns raw (encrypted) credentials — no SecretKey needed here.
         * The frontend decrypts them with WebCrypto API.
         */
        List<CredentialsDTO> credentials = passwordCredentialService.findRawByUser(userId);
        return ResponseEntity.ok(ApiResponseDTO.ok(credentials));
    }

    /**
     * POST /api/credentials
     * Add a new credential.
     * The password field must arrive already AES-GCM encrypted (done in the Vue
     * frontend).
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CredentialsDTO>> add(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody PasswordCredentialRequestDTO dto,
            HttpServletRequest request) {

        Long userId = claims.get("userId", Long.class);
        User user = userService.getUserById(userId).orElseThrow();

        PasswordCredential newCredential = new PasswordCredential();
        newCredential.setSite(dto.getSite());
        newCredential.setUsername(dto.getUsername());
        newCredential.setPassword(dto.getPassword()); // already encrypted by the frontend
        newCredential.setCreatedAt(LocalDateTime.now().toString());
        newCredential.setUpdatedAt(LocalDateTime.now().toString());
        newCredential.setUser(user);

        PasswordCredential saved = passwordCredentialService.save(newCredential);

        // Email notification
        try {
            MessageEntity message = buildMessage(
                    user.getEmail(),
                    "New password added",
                    "A new password has been added to your account.<br>"
                            + "<strong>Site:</strong> " + dto.getSite()
                            + "<br><strong>Username:</strong> " + dto.getUsername(),
                    request);
            emailService.sendConfirmationEmail(user.getEmail(), message);
        } catch (Exception ignored) {
        }

        CredentialsDTO responseDTO = mapToDTO(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Credential added", responseDTO));
    }

    /**
     * GET /api/credentials/{id}
     * Returns a single credential by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CredentialsDTO>> getById(
            @AuthenticationPrincipal Claims claims,
            @PathVariable String id) {
        Long userId = claims.get("userId", Long.class);

        Optional<PasswordCredential> credential = passwordCredentialService.getCredentialById(UUID.fromString(id));

        if (credential.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Credential not found"));
        }

        if (!credential.get().getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.error("Access denied"));
        }

        return ResponseEntity.ok(ApiResponseDTO.ok(mapToDTO(credential.get())));
    }

    /**
     * PUT /api/credentials/{id}
     * Update a credential.
     * The password field must arrive already AES-GCM encrypted.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CredentialsDTO>> update(
            @AuthenticationPrincipal Claims claims,
            @PathVariable String id,
            @RequestBody PasswordCredentialRequestDTO dto,
            HttpServletRequest request) {

        Long userId = claims.get("userId", Long.class);

        Optional<PasswordCredential> credential = passwordCredentialService.getCredentialById(UUID.fromString(id));

        if (credential.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Credential not found"));
        }

        // Ownership check
        Long ownerId = credential.get().getUser().getId();
        if (ownerId == null || !ownerId.toString().equals(String.valueOf(userId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.error("Access denied"));
        }

        credential.get().setSite(dto.getSite());
        credential.get().setUsername(dto.getUsername());
        credential.get().setPassword(dto.getPassword()); // already encrypted
        credential.get().setUpdatedAt(LocalDateTime.now().toString());

        PasswordCredential updated = passwordCredentialService.save(credential.get());

        // Email notification
        try {
            User user = userService.getUserById(userId).orElseThrow();
            MessageEntity message = buildMessage(
                    user.getEmail(),
                    "Password updated",
                    "A password has been updated.<br>"
                            + "<strong>Site:</strong> " + updated.getSite()
                            + "<br><strong>Username:</strong> " + updated.getUsername(),
                    request);
            emailService.sendConfirmationEmail(user.getEmail(), message);
        } catch (Exception ignored) {
        }

        return ResponseEntity.ok(ApiResponseDTO.ok("Credential updated", mapToDTO(updated)));
    }

    /**
     * DELETE /api/credentials/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(
            @AuthenticationPrincipal Claims claims,
            @PathVariable String id) {

        Long userId = claims.get("userId", Long.class);

        Optional<PasswordCredential> credential = passwordCredentialService.getCredentialById(UUID.fromString(id));

        if (credential.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Credential not found"));
        }

        if (!credential.get().getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseDTO.error("Access denied"));
        }

        passwordCredentialService.deleteCredentialById(credential.get().getId());
        return ResponseEntity.ok(ApiResponseDTO.ok("Credential deleted", null));
    }

    // --- Helpers ---

    private CredentialsDTO mapToDTO(PasswordCredential pc) {
        CredentialsDTO dto = new CredentialsDTO();
        dto.setId(pc.getId());
        dto.setSite(pc.getSite());
        dto.setUsername(pc.getUsername());
        dto.setPassword(pc.getPassword()); // still encrypted
        dto.setCreatedAt(pc.getCreatedAt());
        dto.setUpdatedAt(pc.getUpdatedAt());
        return dto;
    }

    private MessageEntity buildMessage(String recipient, String subject,
            String body, HttpServletRequest request) {
        MessageEntity msg = new MessageEntity();
        msg.setRecipient(recipient);
        msg.setSubject(subject);
        msg.setBody(body);
        msg.setUrl("http://localhost:5173/");
        msg.setButtonText("Go to SaveMyPass.dev");
        msg.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        msg.setDevice(request.getHeader("User-Agent"));
        msg.setIp(request.getRemoteAddr());
        msg.setLocation(request.getRemoteHost());
        return msg;
    }
}
