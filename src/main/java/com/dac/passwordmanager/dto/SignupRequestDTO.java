package com.dac.passwordmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    private String tarjetaEmpleado;
    private String cedula;
    private String nombreCompleto;
    private String email;
    private String password;
    private String passwordConfirmacion;

    public SignupRequestDTO() {}

    public SignupRequestDTO(String tarjetaEmpleado, String cedula, String nombreCompleto, String email, String password, String passwordConfirmacion) {
        this.tarjetaEmpleado = tarjetaEmpleado;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.passwordConfirmacion = passwordConfirmacion;
    }
}