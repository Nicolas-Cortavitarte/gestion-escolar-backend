package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.DocenteRequestDto;
import com.colegio.api.dtos.DocenteResponseDto;
import com.colegio.api.dtos.DocenteUpdateDto;
import com.colegio.api.models.Docente;

@Component
public class DocenteMapper {

    public DocenteResponseDto toDto(Docente docente) {
        if (docente == null) {
            return null;
        }

        DocenteResponseDto responseDto = new DocenteResponseDto();
        responseDto.setId(docente.getId());
        responseDto.setUsuarioId(docente.getUsuario().getId());
        responseDto.setDni(docente.getDni());
        responseDto.setNombres(docente.getNombres());
        responseDto.setApellidos(docente.getApellidos());
        responseDto.setSueldoMensual(docente.getSueldoMensual());
        responseDto.setActivo(docente.getUsuario().getActivo());

        return responseDto;
    }

    public Docente toEntity(DocenteRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Docente entity = new Docente();
        entity.setDni(requestDto.getDni());
        entity.setNombres(requestDto.getNombres());
        entity.setApellidos(requestDto.getApellidos());
        entity.setSueldoMensual(requestDto.getSueldoMensual());

        return entity;
    }

    public void updateEntityFromDto(DocenteUpdateDto requestDto, Docente docente) {
        if (requestDto == null || docente == null) {
            return;
        }

        docente.setNombres(requestDto.getNombres());
        docente.setApellidos(requestDto.getApellidos());
        docente.setSueldoMensual(requestDto.getSueldoMensual());
    }
}
