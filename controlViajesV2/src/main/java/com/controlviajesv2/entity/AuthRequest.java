package com.controlviajesv2.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String nombre;
    private String password;
    private String email;
    private String rol;
}
