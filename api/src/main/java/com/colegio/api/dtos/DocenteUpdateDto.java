package com.colegio.api.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocenteUpdateDto {

    @NotNull(message = "Los nombres son obligatorios")
    @Size(min = 3, max = 100, message = "Los nombres deben tener entre 3 y 100 caracteres")
    private String nombres;

    @NotNull(message = "Los apellidos son obligatorios")
    @Size(min = 3, max = 100, message = "Los apellidos deben tener entre 3 y 100 caracteres")
    private String apellidos;

    @NotNull(message = "El sueldo mensual es obligatorio")
    private BigDecimal sueldoMensual;

}
