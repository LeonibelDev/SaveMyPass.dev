/*
* Change user personal access: password and password confirmation
*/
package com.dac.passwordmanager.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class ChangeUserPassword {

    @NotBlank(message = "Password is required")
    private String oldPassword;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String newPassword;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password confirmation must be between 8 and 255 characters")
    private String confirmNewPassword;
}
