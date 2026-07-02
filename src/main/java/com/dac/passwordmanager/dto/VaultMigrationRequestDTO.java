package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class VaultMigrationRequestDTO {
    private String encryptedVaultKey;
    private String recoveryVaultKey;
    
    // For existing users, they also need to re-encrypt their data one last time
    // using the new Vault Key during the migration.
    private List<VaultRotationRequestDTO.CredentialRotationDTO> credentials;
    private List<VaultRotationRequestDTO.NoteRotationDTO> notes;
}
