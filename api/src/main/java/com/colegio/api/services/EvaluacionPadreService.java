package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.EvaluacionPadreRequestDto;
import com.colegio.api.dtos.EvaluacionPadreResponseDto;

public interface EvaluacionPadreService {

    List<EvaluacionPadreResponseDto> obtenerPorEstudiante(UUID estudianteId);

    EvaluacionPadreResponseDto registrarOActualizar(UUID estudianteId, EvaluacionPadreRequestDto requestDto);
}
