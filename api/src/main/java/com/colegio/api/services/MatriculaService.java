package com.colegio.api.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;

public interface MatriculaService {

    List<MatriculaResponseDto> obtenerTodos();

    List<MatriculaResponseDto> obtenerPorEstudiante(UUID estudianteId);

    MatriculaResponseDto crear(MatriculaRequestDto requestDto);

    MatriculaResponseDto actualizar(UUID id, MatriculaRequestDto requestDto);

    MatriculaResponseDto marcarMatriculaComoPagada(UUID id, OffsetDateTime fechaPago);

    void eliminar(UUID id);

}
