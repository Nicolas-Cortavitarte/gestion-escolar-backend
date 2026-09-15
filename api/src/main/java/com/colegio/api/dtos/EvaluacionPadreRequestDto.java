package com.colegio.api.dtos;

import com.colegio.api.models.NotaCualitativa;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluacionPadreRequestDto {

    @NotNull(message = "El bimestre es obligatorio")
    @Min(value = 1, message = "El bimestre debe estar entre 1 y 4")
    @Max(value = 4, message = "El bimestre debe estar entre 1 y 4")
    private Integer bimestre;

    private NotaCualitativa enviaPuntualmenteHijo;
    private NotaCualitativa apoyaTareasCasa;
    private NotaCualitativa enviaHijoUniformado;
    private NotaCualitativa asisteReunionesColegio;
    private NotaCualitativa cumplePagosInstitucion;
}
