package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;
import com.colegio.api.models.Matricula;

@Component
public class MatriculaMapper {

    public MatriculaResponseDto toDto(Matricula matricula) {

        if (matricula == null) {
            return null;
        }

        MatriculaResponseDto response = new MatriculaResponseDto();
        response.setEstudianteId(matricula.getEstudiante().getId());
        response.setAnioLectivo(matricula.getAnioLectivo());
        response.setNombreEstudiante(
                matricula.getEstudiante().getNombres() + " " + matricula.getEstudiante().getApellidos());
        response.setNivel(matricula.getNivel());
        response.setGrado(matricula.getGrado());
        response.setMontoMatricula(matricula.getMontoMatricula());
        response.setMontoPensionMensual(matricula.getMontoPensionMensual());
        response.setFechaVencimiento(matricula.getDiaVencimientoPension());
        response.setFechaRegistro(matricula.getFechaRegistro());
        response.setMatriculaPagada(matricula.getMatriculaPagada());
        response.setFechaPagoMatricula(matricula.getFechaPagoMatricula());

        return response;
    }

    public Matricula toEntity(MatriculaRequestDto requestDto) {

        if (requestDto == null) {
            return null;
        }

        Matricula matricula = new Matricula();
        matricula.setAnioLectivo(requestDto.getAnioLectivo());
        matricula.setNivel(requestDto.getNivel());
        matricula.setGrado(requestDto.getGrado());
        matricula.setMontoMatricula(requestDto.getMontoMatricula());
        matricula.setMontoPensionMensual(requestDto.getMontoPensionMensual());
        matricula.setDiaVencimientoPension(requestDto.getFechaVencimiento());

        return matricula;
    }

    public void updateEntityFromDto(MatriculaRequestDto requestDto, Matricula matricula) {
        if (requestDto == null || matricula == null) {
            return;
        }

        matricula.setAnioLectivo(requestDto.getAnioLectivo());
        matricula.setNivel(requestDto.getNivel());
        matricula.setGrado(requestDto.getGrado());
        matricula.setMontoMatricula(requestDto.getMontoMatricula());
        matricula.setMontoPensionMensual(requestDto.getMontoPensionMensual());
        matricula.setDiaVencimientoPension(requestDto.getFechaVencimiento());
    }
}
