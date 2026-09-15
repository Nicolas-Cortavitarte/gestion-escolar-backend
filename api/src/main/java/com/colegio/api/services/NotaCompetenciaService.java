package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.NotaCompetenciaRequestDto;
import com.colegio.api.dtos.NotaCompetenciaResponseDto;

public interface NotaCompetenciaService {

    List<NotaCompetenciaResponseDto> obtenerPorEstudiante(UUID estudianteId);

    List<NotaCompetenciaResponseDto> obtenerPorEstudianteYBimestre(UUID estudianteId, Integer bimestre);

    NotaCompetenciaResponseDto registrarOActualizar(UUID estudianteId, NotaCompetenciaRequestDto requestDto);

}
