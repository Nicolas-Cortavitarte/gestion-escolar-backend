package com.colegio.api.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.colegio.api.models.RolUsuario;

import lombok.Data;

@Data
public class UsuarioResponseDto {

    private UUID id;
    private String correo;
    private RolUsuario rol;
    private Boolean activo;
    private OffsetDateTime creadoEn;
}
