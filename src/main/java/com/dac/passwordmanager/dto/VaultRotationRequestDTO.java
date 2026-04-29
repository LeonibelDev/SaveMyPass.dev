package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VaultRotationRequestDTO {
    private String newAuthHash;
    private String newSalt;
    private List<CredentialRotationDTO> credentials;
    private List<NoteRotationDTO> notes;

    @Getter
    @Setter
    public static class CredentialRotationDTO {
        private UUID id;
        private String site;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class NoteRotationDTO {
        private UUID id;
        private String title;
        private String content;
        private String category;
    }
}
