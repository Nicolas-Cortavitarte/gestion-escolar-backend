package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.NotaAreaResponseDto;

public interface NotaAreaService {

    List<NotaAreaResponseDto> obtenerPorEstudiante(UUID estudianteId);

    List<NotaAreaResponseDto> obtenerPorEstudianteYBimestre(UUID estudianteId, Integer bimestre);
}
