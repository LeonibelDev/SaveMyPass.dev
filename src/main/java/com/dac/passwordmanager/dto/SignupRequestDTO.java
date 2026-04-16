package com.dac.passwordmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    @NotBlank(message = "Full name is required")
    private String nombreCompleto;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirmacion;

    public SignupRequestDTO() {
    }
}