package com.colegio.api.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocenteRequestDto {

    @NotNull(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    private String dni;

    @NotNull(message = "Los nombres son obligatorios")
    @Size(min = 3, max = 100, message = "Los nombres deben tener entre 3 y 100 caracteres")
    private String nombres;

    @NotNull(message = "Los apellidos son obligatorios")
    @Size(min = 3, max = 100, message = "Los apellidos deben tener entre 3 y 100 caracteres")
    private String apellidos;

    @NotNull(message = "El sueldo mensual es obligatorio")
    private BigDecimal sueldoMensual;

    @Email(message = "El formato del correo es inválido")
    @NotNull(message = "El correo es obligatorio")
    @Size(max = 100, message = "El correo no debe exceder 100 caracteres")
    private String correo;

    @Size(min = 8, max = 150, message = "La contraseña debe tener al menos 8 caracteres")
    @NotNull(message = "La contraseña es obligatoria")
    private String contrasena;

}
