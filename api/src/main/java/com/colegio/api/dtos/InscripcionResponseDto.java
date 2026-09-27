package com.colegio.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InscripcionResponseDto {
    private EstudianteResponseDto estudiante;
    private MatriculaResponseDto matricula;
}
