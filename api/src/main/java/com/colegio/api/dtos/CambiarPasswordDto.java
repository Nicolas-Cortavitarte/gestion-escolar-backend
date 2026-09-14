package com.colegio.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambiarPasswordDto {

    @NotNull(message = "La constraseña actual es obligatoria")
    private String passwordActual;

    @NotNull(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String passwordNueva;
}
