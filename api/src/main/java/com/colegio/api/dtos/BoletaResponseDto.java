package com.colegio.api.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class BoletaResponseDto {

    private UUID estudianteId;
    private String nombreEstudiante;
    private Integer anioLectivo;

    private List<AreaBoletaDto> areas;
    private List<ConductaBimestreDto> conducta;
    private List<EvaluacionPadreBimestreDto> evaluacionPadre;
    private ResumenFinalResponseDto resumenFinal;

}
