package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.CompetenciaRequestDto;
import com.colegio.api.dtos.CompetenciaResponseDto;

public interface CompetenciaService {

    List<CompetenciaResponseDto> obtenerTodasLasCompetencias();

    CompetenciaResponseDto obtenerCompetenciaPorId(UUID id);

    CompetenciaResponseDto crear(CompetenciaRequestDto requestDto);

    CompetenciaResponseDto actualizar(UUID id, CompetenciaRequestDto requestDto);

    void eliminar(UUID id);
}
