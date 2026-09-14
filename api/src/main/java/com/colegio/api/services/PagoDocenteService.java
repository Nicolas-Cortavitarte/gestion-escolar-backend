package com.colegio.api.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.PagoDocenteResponseDto;
import com.colegio.api.models.Docente;

public interface PagoDocenteService {

    List<PagoDocenteResponseDto> obtenerTodos();

    PagoDocenteResponseDto obtenerPorId(UUID id);

    List<PagoDocenteResponseDto> obtenerPorDocente(UUID id);

    void genererPagoDelAnio(Docente docente, Integer anio);

    PagoDocenteResponseDto marcarComoPagado(UUID id, OffsetDateTime fechaPago);
}
