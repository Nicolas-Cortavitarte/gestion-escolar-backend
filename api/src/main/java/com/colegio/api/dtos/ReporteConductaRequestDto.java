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

    @Min(value = 0, message = "No puede ser negativo")
    private Integer inasistenciasJustificadas = 0;

    @Min(value = 0, message = "No puede ser negativo")
    private Integer inasistenciasInjustificadas = 0;

    @Min(value = 0, message = "No puede ser negativo")
    private Integer tardanzasJustificadas = 0;

    @Min(value = 0, message = "No puede ser negativo")
    private Integer tardanzasInjustificadas = 0;

    @Size(max = 1000, message = "La apreciación no puede exceder 1000 caracteres")
    private String apreciacionTutor;

}
