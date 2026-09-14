package com.colegio.api.dtos;

import java.util.UUID;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class EvaluacionPadreResponseDto {

    private UUID id;
    private UUID estudianteId;
    private Integer bimestre;
    private NotaCualitativa enviaPuntualmenteHijo;
    private NotaCualitativa apoyaTareasCasa;
    private NotaCualitativa enviaHijoUniformado;
    private NotaCualitativa asisteReunionesColegio;
    private NotaCualitativa cumplePagosInstitucion;
}
