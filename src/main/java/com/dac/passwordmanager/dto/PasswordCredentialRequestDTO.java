package com.dac.passwordmanager.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCredentialRequestDTO {
    private UUID id;
    private String site;
    private String username;
    private String password;

    public PasswordCredentialRequestDTO() {
    }
}
