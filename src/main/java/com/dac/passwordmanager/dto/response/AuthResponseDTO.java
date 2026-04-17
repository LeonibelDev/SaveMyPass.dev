package com.dac.passwordmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private Long userId;
    private String email;
    private String name;
    /**
     * Base64-encoded salt used by the Vue frontend to derive the AES key
     * via WebCrypto PBKDF2. The raw key never leaves the browser.
     */
    private String salt;
}
