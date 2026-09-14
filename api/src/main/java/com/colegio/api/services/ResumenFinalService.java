package com.colegio.api.services;

import java.util.UUID;

import com.colegio.api.dtos.ResumenFinalResponseDto;

public interface ResumenFinalService {

    ResumenFinalResponseDto obtenerPorEstudianteYAnio(UUID estudianteId, Integer anioLectivo);

    ResumenFinalResponseDto calcularYGuardar(UUID estudianteId, Integer anioLectivo);
}
