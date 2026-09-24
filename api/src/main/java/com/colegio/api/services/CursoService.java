package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.CursoRequestDto;
import com.colegio.api.dtos.CursoResponseDto;

public interface CursoService {

    public List<CursoResponseDto> obtenerTodos();

    public CursoResponseDto obtenerPorId(UUID id);

    public CursoResponseDto crear(CursoRequestDto cursoRequestDto);

    public CursoResponseDto actualizar(UUID id, CursoRequestDto cursoRequestDto);

    public void eliminar(UUID id);

    public List<CursoResponseDto> obtenerPorDocente(UUID id);

}
