package com.colegio.api.dtos;

import java.util.Map;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class CompetenciaBoletaDto {

    private String nombreCompetencia;
    private Map<Integer, NotaCualitativa> notasPorBimestre;
    private NotaCualitativa promedioFinal;

}
