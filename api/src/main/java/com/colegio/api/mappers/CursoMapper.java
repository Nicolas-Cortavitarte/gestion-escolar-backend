package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.CursoRequestDto;
import com.colegio.api.dtos.CursoResponseDto;
import com.colegio.api.models.Curso;

@Component
public class CursoMapper {

    public CursoResponseDto toDto(Curso curso) {
        if (curso == null) {
            return null;
        }

        CursoResponseDto responseDto = new CursoResponseDto();
        responseDto.setId(curso.getId());
        responseDto.setNombre(curso.getNombre());
        responseDto.setNivel(curso.getNivel());
        responseDto.setGrado(curso.getGrado());
        responseDto.setAnioLectivo(curso.getAnioLectivo());

        if (curso.getDocente() != null) {
            responseDto.setDocenteId(curso.getDocente().getId());
            responseDto.setNombresDocente(
                    curso.getDocente().getNombres() + " " + curso.getDocente().getApellidos());
        }

        return responseDto;
    }

    public Curso toEntity(CursoRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Curso entity = new Curso();
        entity.setNombre(requestDto.getNombre());
        entity.setNivel(requestDto.getNivel());
        entity.setGrado(requestDto.getGrado());
        entity.setAnioLectivo(requestDto.getAnioLectivo());

        return entity;
    }

    public void updateEntityFromDto(CursoRequestDto requestDto, Curso entity) {
        if (requestDto == null || entity == null) {
            return;
        }

        entity.setNombre(requestDto.getNombre());
        entity.setNivel(requestDto.getNivel());
        entity.setGrado(requestDto.getGrado());
        entity.setAnioLectivo(requestDto.getAnioLectivo());
    }

}
