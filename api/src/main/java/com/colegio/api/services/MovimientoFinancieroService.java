package com.colegio.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.MovimientoFinancieroRequestDto;
import com.colegio.api.dtos.MovimientoFinancieroResponseDto;

public interface MovimientoFinancieroService {

    List<MovimientoFinancieroResponseDto> obtenerPorRangoFechas(LocalDate inicio, LocalDate fin);

    MovimientoFinancieroResponseDto crear(MovimientoFinancieroRequestDto requestDto);

    void eliminar(UUID id);
}
