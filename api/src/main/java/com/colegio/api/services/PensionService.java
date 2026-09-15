package com.colegio.api.services;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.colegio.api.dtos.PensionResponseDto;
import com.colegio.api.models.Matricula;

public interface  PensionService {

    List<PensionResponseDto> obtenerTodos();

    PensionResponseDto obtenerPorId(UUID id);

    List<PensionResponseDto> obtenerPorMatricula(UUID id);
    
    void generarPensionesDelAnio(Matricula matricula);

    PensionResponseDto marcarComoPagado(UUID id, OffsetDateTime fechaPago);
    
    PensionResponseDto actualizarMora(UUID id, BigDecimal nuevaMora);
    
}
