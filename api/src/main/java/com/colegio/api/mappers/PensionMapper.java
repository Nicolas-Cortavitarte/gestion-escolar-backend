package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.PensionRequestDto;
import com.colegio.api.dtos.PensionResponseDto;
import com.colegio.api.models.Pension;

@Component
public class PensionMapper {

    public PensionResponseDto toDto(Pension pension) {
        if (pension == null) {
            return null;
        }

        PensionResponseDto dto = new PensionResponseDto();
        dto.setId(pension.getId());
        dto.setMatriculaId(pension.getMatricula().getId());
        dto.setMes(pension.getMes());
        dto.setMontoBase(pension.getMontoBase());
        dto.setMoraAcumulada(pension.getMoraAcumulada());
        dto.setMontoTotal(pension.getMontoTotal());
        dto.setFechaVencimiento(pension.getFechaVencimiento());
        dto.setFechaPago(pension.getFechaPago());
        dto.setEstado(pension.getEstado());

        return dto;
    }

    public Pension toEntity(PensionRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Pension pension = new Pension();
        pension.setMes(dto.getMes());
        pension.setMontoBase(dto.getMontoBase());
        pension.setFechaVencimiento(dto.getFechaVencimiento());
        
        return pension;
    }
}
