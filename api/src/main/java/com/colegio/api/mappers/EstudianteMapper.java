package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.EstudianteRequestDto;
import com.colegio.api.dtos.EstudianteResponseDto;
import com.colegio.api.models.Apoderado;
import com.colegio.api.models.Estudiante;

@Component
public class EstudianteMapper {

    public EstudianteResponseDto toDto(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }

        EstudianteResponseDto responseDto = new EstudianteResponseDto();
        responseDto.setId(estudiante.getId());
        responseDto.setNombres(estudiante.getNombres());
        responseDto.setApellidos(estudiante.getApellidos());
        responseDto.setFechaNacimiento(estudiante.getFechaNacimiento());
        responseDto.setDireccion(estudiante.getDireccion());

        if (estudiante.getApoderado() != null) {
            responseDto.setApoderadoId(estudiante.getApoderado().getId());
            responseDto.setNombreApoderado(
                    estudiante.getApoderado().getNombres() + " " + estudiante.getApoderado().getApellidos());
        }

        responseDto.setCreadoEn(estudiante.getCreadoEn());
        return responseDto;
    }

    public Estudiante toEntity(EstudianteRequestDto requestDto, Apoderado apoderado) {
        if (requestDto == null) {
            return null;
        }

        Estudiante entity = new Estudiante();
        entity.setDni(requestDto.getDni());
        entity.setNombres(requestDto.getNombres());
        entity.setApellidos(requestDto.getApellidos());
        entity.setFechaNacimiento(requestDto.getFechaNacimiento());
        entity.setDireccion(requestDto.getDireccion());
        entity.setApoderado(apoderado);

        return entity;
    }

    public void updateEntityFromDto(EstudianteRequestDto requestDto, Estudiante estudiante, Apoderado apoderado) {
        if (requestDto == null || estudiante == null) {
            return;
        }

        estudiante.setDni(requestDto.getDni());
        estudiante.setNombres(requestDto.getNombres());
        estudiante.setApellidos(requestDto.getApellidos());
        estudiante.setFechaNacimiento(requestDto.getFechaNacimiento());
        estudiante.setDireccion(requestDto.getDireccion());
        estudiante.setApoderado(apoderado);
    }
}
