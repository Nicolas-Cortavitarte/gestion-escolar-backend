package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.ResumenFinalResponseDto;
import com.colegio.api.models.ResumenFinalEstudiante;

@Component
public class ResumenFinalMapper {

    public ResumenFinalResponseDto toDto(ResumenFinalEstudiante entity) {
        if (entity == null) {
            return null;
        }

        ResumenFinalResponseDto dto = new ResumenFinalResponseDto();
        dto.setId(entity.getId());
        dto.setEstudianteId(entity.getEstudiante().getId());
        dto.setNombreEstudiante(
                entity.getEstudiante().getNombres() + " " + entity.getEstudiante().getApellidos());
        dto.setAnioLectivo(entity.getAnioLectivo());
        dto.setSituacionFinal(entity.getSituacionFinal());
        dto.setAreaARecuperar(entity.getAreaARecuperar());
        dto.setCreadoEn(entity.getCreadoEn());

        return dto;
    }
}
