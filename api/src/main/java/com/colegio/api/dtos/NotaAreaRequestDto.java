package com.colegio.api.dtos;

import java.util.UUID;

import com.colegio.api.models.NotaCualitativa;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotaAreaRequestDto {

    @NotNull(message = "El curso es obligatorio")
    private UUID cursoId;

    @NotNull(message = "El bimestre es obligatorio")
    @Min(value = 1, message = "El bimestre debe ser mayor o igual a 1")
    @Max(value = 4, message = "El bimestre debe ser menor o igual a 4")
    private Integer bimestre;

    @NotNull(message = "El calificativo es obligatorio")
    private NotaCualitativa calificativoArea;

}
