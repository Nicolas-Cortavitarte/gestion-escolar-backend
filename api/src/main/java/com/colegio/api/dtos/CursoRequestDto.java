package com.colegio.api.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CursoRequestDto {

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El nivel es obligatorio")
    @Size(min = 3, max = 20, message = "El nivel debe tener entre 3 y 20 caracteres")
    private String nivel;

    @NotNull(message = "El grado es obligatorio")
    @Size(min = 3, max = 50, message = "El grado debe tener entre 3 y 50 caracteres")
    private String grado;

    @NotNull(message = "El año lectivo es obligatorio")
    private Integer anioLectivo;

    private UUID docenteId;
}
