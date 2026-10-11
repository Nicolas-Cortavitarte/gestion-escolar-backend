package com.colegio.api.dtos;

import com.colegio.api.models.NotaCualitativa;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReporteConductaRequestDto {

    @NotNull(message = "El bimestre es obligatorio")
    @Min(value = 1, message = "El bimestre debe estar entre 1 y 4")
    @Max(value = 4, message = "El bimestre debe estar entre 1 y 4")
    private Integer bimestre;

    private NotaCualitativa conductaPuntualidadRespeto;
    private NotaCualitativa conductaActitudAula;
    private NotaCualitativa conductaPresentacionAseo;

    @NotNull(message = "Las inasistencias justificadas son obligatorias")
    @Min(value = 0, message = "Las inasistencias justificadas no pueden ser negativas")
    private Integer inasistenciasJustificadas = 0;

    @NotNull(message = "Las inasistencias injustificadas son obligatorias")
    @Min(value = 0, message = "Las inasistencias injustificadas no pueden ser negativas")
    private Integer inasistenciasInjustificadas = 0;

    @NotNull(message = "Las tardanzas justificadas son obligatorias")
    @Min(value = 0, message = "Las tardanzas justificadas no pueden ser negativas")
    private Integer tardanzasJustificadas = 0;

    @NotNull(message = "Las tardanzas injustificadas son obligatorias")
    @Min(value = 0, message = "Las tardanzas injustificadas no pueden ser negativas")
    private Integer tardanzasInjustificadas = 0;

    @Size(max = 1000, message = "La apreciación no puede exceder 1000 caracteres")
    private String apreciacionTutor;

    @NotNull(message = "El año lectivo es obligatorio")
    @Min(value = 1, message = "El año lectivo debe ser positivo")
    private Integer anioLectivo;

}
