package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String tarjetaEmpleado;
    private String password;

    public LoginRequestDTO() {
    }
}
