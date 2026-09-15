package com.colegio.api.services;

import java.util.UUID;

import com.colegio.api.dtos.BoletaResponseDto;

public interface BoletaService {

    BoletaResponseDto generarBoleta(UUID estudianteId, Integer anioLectivo);

}
