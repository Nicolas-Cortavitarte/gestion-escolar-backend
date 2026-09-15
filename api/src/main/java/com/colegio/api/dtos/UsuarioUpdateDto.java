package com.colegio.api.dtos;

import com.colegio.api.models.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateDto {

    @Email(message = "El formato del correo es inválido")
    @NotNull(message = "El correo es obligatorio")
    @Size(max = 150, message = "El correo debe tener como máximo 150 caracteres")
    private String correo;

    @NotNull(message = "El idRol es obligatorio")
    private RolUsuario rol;
}
