package com.dac.passwordmanager.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCredentialRequestDTO {
    private UUID id;

    @NotBlank(message = "Site is required")
    @Size(max = 255, message = "Site must not exceed 255 characters")
    private String site;

    @NotBlank(message = "Username is required")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 2000, message = "Encrypted password too long")
    private String password;

    public PasswordCredentialRequestDTO() {
    }
}
