package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String tarjetaEmpleado;
    private String cedula;
    private String nombreCompleto;
    private String email;

    public UserDTO() {
    }
}
