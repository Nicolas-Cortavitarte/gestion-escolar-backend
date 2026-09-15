package com.colegio.api.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class CompetenciaResponseDto {

    private UUID id;
    private UUID cursoId;
    private String nombreCompetencia;

}
