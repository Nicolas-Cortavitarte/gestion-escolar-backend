package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.EvaluacionPadreResponseDto;
import com.colegio.api.models.EvaluacionPadreFamilia;

@Component
public class EvaluacionPadreMapper {

    public EvaluacionPadreResponseDto toDto(EvaluacionPadreFamilia entity) {
        if (entity == null)
            return null;

        EvaluacionPadreResponseDto dto = new EvaluacionPadreResponseDto();
        dto.setId(entity.getId());
        dto.setEstudianteId(entity.getEstudiante().getId());
        dto.setBimestre(entity.getBimestre());
        dto.setEnviaPuntualmenteHijo(entity.getEnviaPuntualmenteHijo());
        dto.setApoyaTareasCasa(entity.getApoyaTareasCasa());
        dto.setEnviaHijoUniformado(entity.getEnviaHijoUniformado());
        dto.setAsisteReunionesColegio(entity.getAsisteReunionesColegio());
        dto.setCumplePagosInstitucion(entity.getCumplePagosInstitucion());
        return dto;
    }
}
