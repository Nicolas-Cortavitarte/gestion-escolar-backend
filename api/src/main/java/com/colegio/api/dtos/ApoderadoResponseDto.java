package com.colegio.api.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class ApoderadoResponseDto {

    private UUID id;
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String parentesco;

}
