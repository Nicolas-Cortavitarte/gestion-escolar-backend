package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.DocenteRequestDto;
import com.colegio.api.dtos.DocenteResponseDto;
import com.colegio.api.dtos.DocenteUpdateDto;

public interface DocenteService {

    DocenteResponseDto obtenerPorId(UUID id);

    DocenteResponseDto buscarPorDni(String dni);

    List<DocenteResponseDto> obtenerTodos();

    DocenteResponseDto crear(DocenteRequestDto requestDto);

    DocenteResponseDto actualizar(UUID id, DocenteUpdateDto requestDto);

    void desactivar(UUID id);

    void reactivar(UUID id);
}
