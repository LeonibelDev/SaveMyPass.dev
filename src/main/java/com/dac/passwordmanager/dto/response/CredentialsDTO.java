package com.dac.passwordmanager.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialsDTO {
    private UUID id;
    private String site;
    private String username;
    private String password;
    private String createdAt;
    private String updatedAt;

    public CredentialsDTO() {
    }
}
