package com.colegio.api.dtos;

import java.util.UUID;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class ReporteConductaResponseDto {

    private UUID id;
    private UUID estudianteId;
    private Integer bimestre;
    private NotaCualitativa conductaPuntualidadRespeto;
    private NotaCualitativa conductaActitudAula;
    private NotaCualitativa conductaPresentacionAseo;
    private Integer inasistenciasJustificadas;
    private Integer inasistenciasInjustificadas;
    private Integer tardanzasJustificadas;
    private Integer tardanzasInjustificadas;
    private String apreciacionTutor;

}
