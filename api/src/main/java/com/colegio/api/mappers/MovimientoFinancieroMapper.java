package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.MovimientoFinancieroRequestDto;
import com.colegio.api.dtos.MovimientoFinancieroResponseDto;
import com.colegio.api.models.MovimientoFinanciero;

@Component
public class MovimientoFinancieroMapper {

    public MovimientoFinancieroResponseDto toDto(MovimientoFinanciero entity) {
        if (entity == null)
            return null;

        MovimientoFinancieroResponseDto dto = new MovimientoFinancieroResponseDto();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setCategoria(entity.getCategoria());
        dto.setConcepto(entity.getConcepto());
        dto.setMonto(entity.getMonto());
        dto.setFecha(entity.getFecha());
        dto.setDescripcion(entity.getDescripcion());
        dto.setRegistradoPorId(entity.getRegistradoPor().getId());
        dto.setRegistradoPorEmail(entity.getRegistradoPor().getEmail());
        dto.setCreadoEn(entity.getCreadoEn());
        return dto;
    }

    public MovimientoFinanciero toEntity(MovimientoFinancieroRequestDto requestDto) {
        if (requestDto == null)
            return null;

        MovimientoFinanciero entity = new MovimientoFinanciero();
        entity.setTipo(requestDto.getTipo());
        entity.setCategoria(requestDto.getCategoria());
        entity.setConcepto(requestDto.getConcepto());
        entity.setMonto(requestDto.getMonto());
        entity.setFecha(requestDto.getFecha());
        entity.setDescripcion(requestDto.getDescripcion());
        return entity;
    }
}
