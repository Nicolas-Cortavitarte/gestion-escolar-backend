package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.CompetenciaRequestDto;
import com.colegio.api.dtos.CompetenciaResponseDto;
import com.colegio.api.models.Competencia;

@Component
public class CompetenciaMapper {

    public CompetenciaResponseDto toDto(Competencia entity) {
        if (entity == null) {
            return null;
        }

        CompetenciaResponseDto responseDto = new CompetenciaResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setNombreCompetencia(entity.getNombreCompetencia());
        responseDto.setCursoId(entity.getCurso().getId());

        return responseDto;
    }

    public Competencia toEntity(CompetenciaRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Competencia entity = new Competencia();
        entity.setNombreCompetencia(dto.getNombreCompetencia());

        return entity;
    }

    public void updateEntityFromDto(CompetenciaRequestDto requestDto, Competencia entity) {
        if (requestDto == null || entity == null) {
            return;
        }

        entity.setNombreCompetencia(requestDto.getNombreCompetencia());
    }
}
