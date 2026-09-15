package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.NotaAreaResponseDto;
import com.colegio.api.models.NotaArea;

@Component
public class NotaAreaMapper {

    public NotaAreaResponseDto toDto(NotaArea response) {
        if (response == null) {
            return null;
        }

        NotaAreaResponseDto dto = new NotaAreaResponseDto();
        dto.setId(response.getId());
        dto.setEstudianteId(response.getEstudiante().getId());
        dto.setCursoId(response.getCurso().getId());
        dto.setNombreCurso(response.getCurso().getNombre());
        dto.setBimestre(response.getBimestre());
        dto.setCalificativoArea(response.getCalificativoArea());
        return dto;
    }

}
