package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.ReporteConductaResponseDto;
import com.colegio.api.models.ReporteConductaAsistencia;

@Component
public class ReporteConductaMapper {

    public ReporteConductaResponseDto toDto(ReporteConductaAsistencia entity) {
        if (entity == null)
            return null;

        ReporteConductaResponseDto dto = new ReporteConductaResponseDto();
        dto.setId(entity.getId());
        dto.setEstudianteId(entity.getEstudiante().getId());
        dto.setBimestre(entity.getBimestre());
        dto.setConductaPuntualidadRespeto(entity.getConductaPuntualidadRespeto());
        dto.setConductaActitudAula(entity.getConductaActitudAula());
        dto.setConductaPresentacionAseo(entity.getConductaPresentacionAseo());
        dto.setInasistenciasJustificadas(entity.getInasistenciasJustificadas());
        dto.setInasistenciasInjustificadas(entity.getInasistenciasInjustificadas());
        dto.setTardanzasJustificadas(entity.getTardanzasJustificadas());
        dto.setTardanzasInjustificadas(entity.getTardanzasInjustificadas());
        dto.setApreciacionTutor(entity.getApreciacionTutor());
        return dto;
    }

}
