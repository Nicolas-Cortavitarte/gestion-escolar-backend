package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.CambiarPasswordDto;
import com.colegio.api.dtos.UsuarioRequestDto;
import com.colegio.api.dtos.UsuarioResponseDto;
import com.colegio.api.dtos.UsuarioUpdateDto;

public interface UsuarioService {

    List<UsuarioResponseDto> obtenerTodos();

    UsuarioResponseDto obtenerPorId(UUID id);

    UsuarioResponseDto crear(UsuarioRequestDto requestDto);

    UsuarioResponseDto actualizar(UUID id, UsuarioUpdateDto usuarioUpdateDto);

    void cambiarPassword(UUID id, CambiarPasswordDto cambiarPasswordDto);

    void eliminar(UUID id);
}
