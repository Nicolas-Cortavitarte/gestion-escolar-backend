package com.colegio.api.dtos;

import java.util.UUID;

import com.colegio.api.models.NotaCualitativa;

import lombok.Data;

@Data
public class NotaAreaResponseDto {

    private UUID id;
    private UUID estudianteId;
    private UUID cursoId;
    private String nombreCurso;
    private Integer bimestre;
    private NotaCualitativa calificativoArea;

}
