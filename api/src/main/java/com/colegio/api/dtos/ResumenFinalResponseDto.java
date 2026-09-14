package com.colegio.api.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.colegio.api.models.SituacionFinal;

import lombok.Data;

@Data
public class ResumenFinalResponseDto {

    private UUID id;
    private UUID estudianteId;
    private String nombreEstudiante;
    private Integer anioLectivo;
    private SituacionFinal situacionFinal;
    private String areaARecuperar;
    private OffsetDateTime creadoEn;
}
