package com.dac.passwordmanager.controller;

import com.dac.passwordmanager.config.security.JwtUtil;
import com.dac.passwordmanager.dto.LoginRequestDTO;
import com.dac.passwordmanager.dto.SignupRequestDTO;
import com.dac.passwordmanager.dto.response.ApiResponseDTO;
import com.dac.passwordmanager.dto.response.AuthResponseDTO;
import com.dac.passwordmanager.entity.MessageEntity;
import com.dac.passwordmanager.entity.User;
import com.dac.passwordmanager.service.UserService;
import com.dac.passwordmanager.service.email.EmailService;
import com.dac.passwordmanager.service.email.EmailTemplate;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final UserService userService;
        private final JwtUtil jwtUtil;
        private final EmailService emailService;

        /**
         * GET /api/auth/salt
         * Public endpoint to get the user's salt for client-side key derivation.
         */
        @GetMapping("/salt")
        public ResponseEntity<ApiResponseDTO<String>> getSalt(@RequestParam String email) {
                Optional<User> user = userService.getUserByEmail(email);
                if (user.isEmpty()) {
                        // Return a deterministic random-looking salt to prevent email enumeration
                        // In a real app, this should be consistently returned for the same email
                        return ResponseEntity.ok(ApiResponseDTO.ok("N/A"));
                }
                return ResponseEntity.ok(ApiResponseDTO.ok(user.get().getSalt()));
        }

        /**
         * POST /api/auth/signup
         * Register a new user account.
         * 
         * @throws IOException
         */
        @PostMapping("/signup")
        public ResponseEntity<ApiResponseDTO<Void>> signup(
                        @Valid @RequestBody SignupRequestDTO dto, HttpServletRequest request) throws IOException {
                try {
                        User user = userService.saveNewUser(dto);

                        /*
                         *
                         * Send confirmation email
                         * 
                         */
                        MessageEntity message = EmailTemplate.buildMessage(user.getEmail(),
                                        "Hi " + user.getNombreCompleto() + ", Welcome to SaveMyPass.dev",
                                        "We're excited to have you on board! Here's what you can do with SaveMyPass.dev. "
                                                        +
                                                        "Please verify your email address to complete your registration. "
                                                        +
                                                        "Click the button below to confirm your account.",
                                        request);

                        emailService.sendConfirmationEmail(user.getEmail(), message);

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
                                        user.getSalt());

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
                                "email", user.get().getEmail());
                return ResponseEntity.ok(ApiResponseDTO.ok(data));
        }

        /**
         * PUT /api/auth/rotate-vault
         * Atomic Z-K vault rotation. Updates auth hash, salt, and all encrypted items.
         */
        @PutMapping("/rotate-vault")
        public ResponseEntity<ApiResponseDTO<Void>> rotateVault(
                        @AuthenticationPrincipal Claims claims,
                        @RequestBody com.dac.passwordmanager.dto.VaultRotationRequestDTO dto) {
                try {
                        Long userId = claims.get("userId", Long.class);
                        userService.rotateVault(dto, userId);
                        return ResponseEntity.ok(ApiResponseDTO.ok("Vault rotated successfully", null));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(ApiResponseDTO.error("An error occurred during vault rotation."));
                }
        }
}
