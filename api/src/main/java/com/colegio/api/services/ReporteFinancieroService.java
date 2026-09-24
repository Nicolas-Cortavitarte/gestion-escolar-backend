package com.colegio.api.services;

import java.time.LocalDate;

import com.colegio.api.dtos.ReporteFinancieroResponseDto;

public interface ReporteFinancieroService {
    ReporteFinancieroResponseDto generarReporte(LocalDate fechaInicio, LocalDate fechaFin);
}
