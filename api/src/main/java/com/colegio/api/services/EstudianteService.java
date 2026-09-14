package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.EstudianteRequestDto;
import com.colegio.api.dtos.EstudianteResponseDto;

public interface EstudianteService {

    List<EstudianteResponseDto> obtenerTodos();

    EstudianteResponseDto obtenerPorId(UUID id);

    EstudianteResponseDto crear(EstudianteRequestDto requestDto);

    EstudianteResponseDto actualizar(UUID id, EstudianteRequestDto requestDto);

    void eliminar(UUID id);
}
