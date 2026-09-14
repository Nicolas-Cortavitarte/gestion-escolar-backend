package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.PagoDocenteRequestDto;
import com.colegio.api.dtos.PagoDocenteResponseDto;
import com.colegio.api.models.PagoDocente;

@Component
public class PagoDocenteMapper {

    public PagoDocenteResponseDto toDto(PagoDocente pagoDocente) {
        if (pagoDocente == null) {
            return null;
        }

        PagoDocenteResponseDto dto = new PagoDocenteResponseDto();
        dto.setId(pagoDocente.getId());
        dto.setDocenteId(pagoDocente.getDocente().getId());
        dto.setNombresDocente(pagoDocente.getDocente().getNombres() + " "
                + pagoDocente.getDocente().getApellidos());
        dto.setMes(pagoDocente.getMes());
        dto.setAnio(pagoDocente.getAnio());
        dto.setMonto(pagoDocente.getMonto());
        dto.setFechaProgramada(pagoDocente.getFechaProgramada());
        dto.setFechaPago(pagoDocente.getFechaPago());
        dto.setEstado(pagoDocente.getEstado());

        return dto;
    }

    public PagoDocente toEntity(PagoDocenteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        PagoDocente pagoDocente = new PagoDocente();
        pagoDocente.setMes(dto.getMes());
        pagoDocente.setAnio(dto.getAnio());
        pagoDocente.setMonto(dto.getMonto());
        pagoDocente.setFechaProgramada(dto.getFechaProgramada());

        return pagoDocente;
    }

}
