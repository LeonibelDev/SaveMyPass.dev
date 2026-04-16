/*
* Change user personal access: password and password confirmation
*/
package com.dac.passwordmanager.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangeUserPassword {

    @NotBlank(message = "Password is required")
    private String oldPassword;

    @NotBlank(message = "Password is required")
    private String newPassword;

    @NotBlank(message = "Password is required")
    private String confirmNewPassword;
}
