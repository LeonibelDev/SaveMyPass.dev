package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {
    private String token;
    private String newAuthHash;
    private String newSalt;
    private String newEncryptedVaultKey;
}
