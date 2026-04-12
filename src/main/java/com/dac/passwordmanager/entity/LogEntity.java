package com.dac.passwordmanager.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEntity {
    private String tarjetaEmpleado;
    private String action;
    private String date;
    private String ip;
    private String location;
    private String device;

    public LogEntity() {
    }
}
