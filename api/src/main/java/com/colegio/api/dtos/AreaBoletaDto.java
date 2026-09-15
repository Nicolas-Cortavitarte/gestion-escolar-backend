package com.colegio.api.dtos;

import java.util.List;
import java.util.Map;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class AreaBoletaDto {

    private String nombreArea;
    private List<CompetenciaBoletaDto> competencias;
    private Map<Integer, NotaCualitativa> calificativoAreaPorBimestre;
    private NotaCualitativa promedioFinalArea;

}