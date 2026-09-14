package com.colegio.api.dtos;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class ConductaBimestreDto {

    private Integer bimestre;
    private NotaCualitativa puntualidadRespeto;
    private NotaCualitativa actitudAula;
    private NotaCualitativa presentacionAseo;
    private Integer inasistenciasJustificadas;
    private Integer inasistenciasInjustificadas;
    private Integer tardanzasJustificadas;
    private Integer tardanzasInjustificadas;
    private String apreciacionTutor;

}
