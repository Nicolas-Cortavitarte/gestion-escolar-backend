package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.ApoderadoRequestDto;
import com.colegio.api.dtos.ApoderadoResponseDto;
import com.colegio.api.models.Apoderado;

@Component
public class ApoderadoMapper {

    public ApoderadoResponseDto toDto(Apoderado apoderado) {
        if (apoderado == null) {
            return null;
        }

        ApoderadoResponseDto responseDto = new ApoderadoResponseDto();
        responseDto.setId(apoderado.getId());
        responseDto.setDni(apoderado.getDni());
        responseDto.setNombre(apoderado.getNombres());
        responseDto.setApellidos(apoderado.getApellidos());

        responseDto.setTelefono(apoderado.getTelefono());
        responseDto.setEmail(apoderado.getEmail());
        responseDto.setParentesco(apoderado.getParentesco());

        return responseDto;
    }

    public Apoderado toEntity(ApoderadoRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Apoderado entity = new Apoderado();
        entity.setDni(requestDto.getDni());
        entity.setNombres(requestDto.getNombre());
        entity.setApellidos(requestDto.getApellidos());
        entity.setTelefono(requestDto.getTelefono());
        entity.setEmail(requestDto.getEmail());
        entity.setParentesco(requestDto.getParentesco());

        return entity;
    }
}
