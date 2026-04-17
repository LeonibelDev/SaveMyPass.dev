package com.dac.passwordmanager.controller;

import com.dac.passwordmanager.config.security.JwtUtil;
import com.dac.passwordmanager.config.security.KeyUtil;
import com.dac.passwordmanager.dto.ChangeUserPassword;
import com.dac.passwordmanager.dto.LoginRequestDTO;
import com.dac.passwordmanager.dto.SignupRequestDTO;
import com.dac.passwordmanager.dto.response.ApiResponseDTO;
import com.dac.passwordmanager.dto.response.AuthResponseDTO;
import com.dac.passwordmanager.entity.User;
import com.dac.passwordmanager.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * POST /api/auth/signup
     * Register a new user account.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<Void>> signup(
            @Valid @RequestBody SignupRequestDTO dto) {
        try {
            userService.saveNewUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.ok("Account created successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login
     * Returns JWT token + user salt for AES key derivation on the frontend.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @RequestBody LoginRequestDTO dto) {
        try {
            User user = userService.login(dto);
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());

            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getNombreCompleto(),
                    user.getSalt()   // frontend derives AES key locally with PBKDF2
            );

            return ResponseEntity.ok(ApiResponseDTO.ok(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    /**
     * GET /api/auth/me
     * Returns authenticated user profile data.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> me(
            @AuthenticationPrincipal Claims claims) {
        Long userId = claims.get("userId", Long.class);

        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("User not found"));
        }

        Map<String, Object> data = Map.of(
                "id", user.get().getId(),
                "name", user.get().getNombreCompleto(),
                "email", user.get().getEmail()
        );
        return ResponseEntity.ok(ApiResponseDTO.ok(data));
    }

    /**
     * PUT /api/auth/password
     * Change password and re-encrypt vault.
     * The frontend must provide both the old and new AES-encrypted payloads.
     *
     * Body: { oldPassword, newPassword, confirmNewPassword, oldSalt, newSalt }
     * Note: The frontend re-derives the AES key after receiving the new salt in the response.
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> changePassword(
            @AuthenticationPrincipal Claims claims,
            @Valid @RequestBody ChangeUserPassword dto) {
        try {
            Long userId = claims.get("userId", Long.class);
            User user = userService.getUserById(userId).orElseThrow();

            /*
             * Derive the OLD secret key (to re-encrypt credentials)
             */
            SecretKey oldSecretKey = KeyUtil.deriveKey(
                    dto.getOldPassword(),
                    Base64.getDecoder().decode(user.getSalt())
            );

            SecretKey newSecretKey = userService.changePassword(dto, userId, oldSecretKey);

            /*
             * Re-fetch user to get updated salt
             */
            User updatedUser = userService.getUserById(userId).orElseThrow();
            String newToken = jwtUtil.generateToken(updatedUser.getId(), updatedUser.getEmail());

            AuthResponseDTO response = new AuthResponseDTO(
                    newToken,
                    updatedUser.getId(),
                    updatedUser.getEmail(),
                    updatedUser.getNombreCompleto(),
                    updatedUser.getSalt()
            );

            return ResponseEntity.ok(ApiResponseDTO.ok("Password updated. Please re-derive your vault key.", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("An unexpected error occurred."));
        }
    }
}
