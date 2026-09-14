package com.colegio.api.dtos;

import java.util.UUID;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class NotaCompetenciaResponseDto {

    private UUID id;
    private UUID estudianteId;
    private UUID competenciaId;
    private String nombreCompetencia;
    private Integer bimestre;
    private NotaCualitativa calificativo;
}
