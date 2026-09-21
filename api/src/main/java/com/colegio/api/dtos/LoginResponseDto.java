package com.colegio.api.dtos;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String token;
    private String correo;
    private String rol;

}
