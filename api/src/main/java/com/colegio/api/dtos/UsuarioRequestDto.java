package com.colegio.api.dtos;

import com.colegio.api.models.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @Email(message = "El formato del correo es inválido")
    @NotNull(message = "El correo es obligatorio")
    @Size(max = 255, message = "El correo no puede exceder 255 caracteres")
    private String correo;

    @Size(min = 8, max = 150, message = "La contraseña debe tener al menos 8 caracteres")
    @NotNull(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}
