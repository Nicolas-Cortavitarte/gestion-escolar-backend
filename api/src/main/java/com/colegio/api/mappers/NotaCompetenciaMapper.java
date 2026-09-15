package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.NotaCompetenciaResponseDto;
import com.colegio.api.models.NotaCompetencia;

@Component
public class NotaCompetenciaMapper {

    public NotaCompetenciaResponseDto toDto(NotaCompetencia notaCompetencia) {
        if (notaCompetencia == null) {
            return null;
        }

        NotaCompetenciaResponseDto dto = new NotaCompetenciaResponseDto();
        dto.setId(notaCompetencia.getId());
        dto.setEstudianteId(notaCompetencia.getEstudiante().getId());
        dto.setCompetenciaId(notaCompetencia.getCompetencia().getId());
        dto.setNombreCompetencia(notaCompetencia.getCompetencia().getNombreCompetencia());
        dto.setBimestre(notaCompetencia.getBimestre());
        dto.setCalificativo(notaCompetencia.getCalificativo());

        return dto;
    }

}
