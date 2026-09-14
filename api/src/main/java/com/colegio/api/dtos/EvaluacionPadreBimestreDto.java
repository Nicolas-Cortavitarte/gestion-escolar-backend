package com.colegio.api.dtos;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class EvaluacionPadreBimestreDto {

    private Integer bimestre;
    private NotaCualitativa enviaPuntualmenteHijo;
    private NotaCualitativa apoyaTareasCasa;
    private NotaCualitativa enviaHijoUniformado;
    private NotaCualitativa asisteReunionesColegio;
    private NotaCualitativa cumplePagosInstitucion;

}
