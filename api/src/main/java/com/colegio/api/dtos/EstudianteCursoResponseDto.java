package com.colegio.api.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class EstudianteCursoResponseDto {

    private UUID estudianteId;
    private String nombreEstudiante;
    private Integer anioLectivo;
    private String nivel;
    private String grado;
}