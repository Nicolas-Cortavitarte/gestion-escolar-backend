package com.colegio.api.services;

import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.ReporteConductaRequestDto;
import com.colegio.api.dtos.ReporteConductaResponseDto;

public interface ReporteConductaService {

    List<ReporteConductaResponseDto> obtenerPorEstudiante(UUID estudianteId);

    ReporteConductaResponseDto registrarOActualizar(UUID estudianteId, ReporteConductaRequestDto requestDto);

}
