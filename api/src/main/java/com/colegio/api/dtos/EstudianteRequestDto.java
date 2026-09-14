package com.colegio.api.dtos;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EstudianteRequestDto {

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    private String dni;

    @NotNull(message = "Los nombres es obligatorio")
    private String nombres;

    @NotNull(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser menor a la fecha actual")
    private LocalDate fechaNacimiento;

    private String direccion;

    private UUID idApoderado;

    @Valid
    private ApoderadoRequestDto apoderadoNuevo;
}
